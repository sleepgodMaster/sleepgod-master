package com.cool.tageventbus;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.cool.tageventbus.meta.SubscriberInfo;
import com.cool.tageventbus.meta.SubscriberInfoIndex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TagEventBus {

    public static TagEventBus instance;
    private final Map<Class<?>, List<SubscriberMethod>> METHOD_CACHE;
    private final Map<Object, CopyOnWriteArrayList<Subscription>> subscriptionsBySubscriber;
    private final CopyOnWriteArrayList<StickyEvent> mStickyEvents;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ExecutorService executorService;
    private final static String TAGEMPTY = "";
    private final static String POSTFIX_BUSINDEX = "_BusIndex";

    private TagEventBus() {
        METHOD_CACHE = new ConcurrentHashMap<>();
        subscriptionsBySubscriber = new HashMap<>();
        mStickyEvents = new CopyOnWriteArrayList<>();
        executorService = Executors.newCachedThreadPool();
    }

    public static TagEventBus getDefault() {
        if (instance == null) {
            synchronized (TagEventBus.class) {
                if (instance == null) {
                    instance = new TagEventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        Class<?> subscriberClass = subscriber.getClass();
        String subscriberClassName;
        while (true) {

            List<SubscriberMethod> subscriberMethods = findSubscriberMethods(subscriberClass);

            synchronized (this) {
                for (SubscriberMethod subscriberMethod : subscriberMethods) {
                    subscribe(subscriber, subscriberMethod);
                }
            }

            subscriberClass = subscriberClass.getSuperclass();
            subscriberClassName = subscriberClass.getName();

            if(subscriberClassName.startsWith("android.") || subscriberClassName.startsWith("java.")
                    || subscriberClassName.startsWith("androidx.")){
                break;
            }
        }

        if (mStickyEvents.size() > 0) {
            subscriptionStickyEvent();
        }
    }

    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsBySubscriber.get(subscriber);
        if (subscriptions == null) {
            subscriptions = new CopyOnWriteArrayList<>();
            subscriptionsBySubscriber.put(subscriber, subscriptions);
        } else {
            if (subscriptions.contains(newSubscription)) {
                throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered");
            }

            for (Subscription subscription : subscriptions) {
                if(subscription.subscriberMethod == subscriberMethod){
                    throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered");
                }
            }
        }
        subscriptions.add(newSubscription);
    }

    private List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {

        List<SubscriberMethod> cacheSubscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (cacheSubscriberMethods != null) {
            return cacheSubscriberMethods;
        }

        List<SubscriberMethod> subscriberMethods = getSubscriberMethodsUseGenerateJavaFile(subscriberClass);
        if (subscriberMethods != null) return subscriberMethods;

        subscriberMethods = getSubscriberMethodsUseReflect(subscriberClass);
        return subscriberMethods;
    }

    private List<SubscriberMethod> getSubscriberMethodsUseReflect(Class<?> subscriberClass) {
        Method[] methods = subscriberClass.getDeclaredMethods();
        List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();

            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe != null) {
                subscriberMethods.add(new SubscriberMethod(method, subscribe.tag(), parameterTypes, subscribe.threadMode()));
            }
        }
        if (!subscriberMethods.isEmpty()) {
            METHOD_CACHE.put(subscriberClass, subscriberMethods);
        }
        return subscriberMethods;
    }

    private List<SubscriberMethod> getSubscriberMethodsUseGenerateJavaFile(Class<?> subscriberClass) {
        String subscriberClassName = subscriberClass.getName();
        try {
            Class<?> busIndexClass = Class.forName(subscriberClassName + POSTFIX_BUSINDEX);
            Object instance = busIndexClass.newInstance();
            SubscriberInfoIndex subscriberInfoIndex = (SubscriberInfoIndex) instance;
            SubscriberInfo subscriberInfo = subscriberInfoIndex.getSubscriberInfo(subscriberClass);
            if (subscriberInfo != null) {
                List<SubscriberMethod> subscriberMethods = subscriberInfo.getSubscriberMethods();
                if (subscriberMethods != null && subscriberMethods.size() > 0) {
                    METHOD_CACHE.put(subscriberClass, subscriberMethods);
                    return subscriberMethods;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void post(Object... events) {
        postByTag(TAGEMPTY, events);
    }


    public void postByTag(String tag, Object... events) {
        if (events == null) {
            return;
        }

        Class<?>[] eventTypes = getEventTypes(events);
        CopyOnWriteArrayList<Subscription> subscriptions = getSubscriptions(eventTypes);
        subscription(tag, subscriptions, events);
    }

    private void subscriptionStickyEvent() {
        CopyOnWriteArrayList<StickyEvent> subscriptionStickyEvents = new CopyOnWriteArrayList<>();
        for (StickyEvent stickyEvent : mStickyEvents) {
            CopyOnWriteArrayList<Subscription> subscriptions = getSubscriptions(stickyEvent.eventTypes);
            subscription(stickyEvent.tag, subscriptions, stickyEvent.events);
            if (subscriptions.size() > 0) {
                subscriptionStickyEvents.add(stickyEvent);
            }
        }

        if (subscriptionStickyEvents.size() > 0) {
            mStickyEvents.removeAll(subscriptionStickyEvents);
        }
    }

    private void subscription(String tag, CopyOnWriteArrayList<Subscription> subscriptions, Object... events) {
        if (subscriptions != null) {
            for (Subscription subscription : subscriptions) {
                postToSubscription(subscription, events, tag);
            }
        }
    }

    private Class<?>[] getEventTypes(Object[] events) {
        Class<?>[] eventTypes = new Class[events.length];
        for (int i = 0; i < events.length; i++) {
            eventTypes[i] = events[i].getClass();
        }
        return eventTypes;
    }

    public void postSticky(Object... events) {
        postStickyByTag(TAGEMPTY, events);
    }

    public void postStickyByTag(String tag, Object... events) {
        if (events == null) {
            return;
        }
        Class<?>[] eventTypes = getEventTypes(events);

        mStickyEvents.add(new StickyEvent(tag, eventTypes, events));
    }

    private void postToSubscription(final Subscription subscription, final Object[] events, String tag) {
        if (TextUtils.equals(subscription.subscriberMethod.tag, tag)) {
            boolean mainThread = isMainThread();
            switch (subscription.subscriberMethod.threadMode) {
                case POSTING:
                    invokeSubscriber(subscription, events);
                    break;
                case MAIN:
                case MAIN_ORDERED:
                    if (mainThread) {
                        invokeSubscriber(subscription, events);
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                invokeSubscriber(subscription, events);
                            }
                        });
                    }
                    break;
                case BACKGROUND:
                    if (mainThread) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                invokeSubscriber(subscription, events);
                            }
                        });
                    } else {
                        invokeSubscriber(subscription, events);
                    }
                    break;
                case ASYNC:
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            invokeSubscriber(subscription, events);
                        }
                    });
                    break;
            }
        }
    }

    private void invokeSubscriber(Subscription subscription, Object... events) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, events);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private CopyOnWriteArrayList<Subscription> getSubscriptions(Class<?>[] eventTypes) {
        CopyOnWriteArrayList<Subscription> subscriptions = new CopyOnWriteArrayList<>();
        Set<Map.Entry<Object, CopyOnWriteArrayList<Subscription>>> entries = subscriptionsBySubscriber.entrySet();

        for (Map.Entry<Object, CopyOnWriteArrayList<Subscription>> entry : entries) {
            CopyOnWriteArrayList<Subscription> subscriptionList = entry.getValue();
            for (Subscription subscription : subscriptionList) {
                Class<?>[] eventTypesClass = subscription.subscriberMethod.eventTypes;
                boolean paramsMatch = isParamsMatch(eventTypes, eventTypesClass);
                if (paramsMatch) {
                    subscriptions.add(subscription);
                }
            }
        }
        return subscriptions;
    }

    private boolean isParamsMatch(Class<?>[] eventTypes, Class<?>[] eventTypesClass) {
        if (eventTypes.length != eventTypesClass.length) {
            return false;
        }

        for (int i = 0; i < eventTypes.length; i++) {
            Class<?> eventType = eventTypes[i];
            if (eventType == int.class) {
                eventTypes[i] = Integer.class;
            } else if (eventType == boolean.class) {
                eventTypes[i] = Boolean.class;
            } else if (eventType == char.class) {
                eventTypes[i] = Character.class;
            } else if (eventType == byte.class) {
                eventTypes[i] = Byte.class;
            } else if (eventType == short.class) {
                eventTypes[i] = Short.class;
            } else if (eventType == long.class) {
                eventTypes[i] = Long.class;
            } else if (eventType == float.class) {
                eventTypes[i] = Float.class;
            } else if (eventType == double.class) {
                eventTypes[i] = Double.class;
            }
        }

        for (int i = 0; i < eventTypesClass.length; i++) {
            Class<?> eventType = eventTypesClass[i];
            if (eventType == int.class) {
                eventTypesClass[i] = Integer.class;
            } else if (eventType == boolean.class) {
                eventTypesClass[i] = Boolean.class;
            } else if (eventType == char.class) {
                eventTypesClass[i] = Character.class;
            } else if (eventType == byte.class) {
                eventTypesClass[i] = Byte.class;
            } else if (eventType == short.class) {
                eventTypesClass[i] = Short.class;
            } else if (eventType == long.class) {
                eventTypesClass[i] = Long.class;
            } else if (eventType == float.class) {
                eventTypesClass[i] = Float.class;
            } else if (eventType == double.class) {
                eventTypesClass[i] = Double.class;
            }
        }

        for (int i = 0; i < eventTypes.length; i++) {

            Class<?> postEventType = eventTypes[i];
            Class<?> receiverEventType = eventTypesClass[i];

            boolean isEqules = postEventType == receiverEventType || receiverEventType.isAssignableFrom(postEventType);

            if (!isEqules) {
                return false;
            }
        }

        return true;
    }

    public void unregister(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsBySubscriber.get(subscriber);
        if (subscriptions != null && subscriptions.size() > 0) {
            unSubscriber(subscriber, subscriptions);
            subscriptionsBySubscriber.remove(subscriber);
        }
    }

    private void unSubscriber(Object subscriber, CopyOnWriteArrayList<Subscription> subscriptions) {
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);
                if (subscription.subscriber == subscriber) {
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
