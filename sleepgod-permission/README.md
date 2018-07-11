# 权限申请框架APermission
> APermission是一个android6.0申请权限的框架，免去写申请权限时的重复性代码,使用起来极其简单，可以说是我目前遇到使用的最方便的框架了，动态授权只需一个注解，而且可以在任何地方申请权限，不再局限于在activity,service中

### 一、既然这么好用，那该如何接入呢
1. 在项目的根目录的build.gradle中引入,注意是项目`根目录`
 ```
dependencies {
        ...
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.1'
    }
```
2. 在项目app的build.gradle中引入
```
apply plugin: 'android-aspectjx'
```
3. 在项目的build.gradle 添加
```
dependencies {
     ...
    implementation 'com.cool:apermission:1.0.2'
}
```

## 二、如何在项目中使用呢
```
@APermission(permissions = {Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION}, deniedMessage = "授权失败")
    public void testActivity(View view) {
        Toast.makeText(this, "授权成功,执行接下来的代码", Toast.LENGTH_SHORT).show();
    }
```
这样一个注解就可以申请权限了，permission必须指定，其它都是可选的，用起来是不是很爽

APermission除了能指定permissions，还有下面功能
* permissions 指名要申请的权限
* deniedMessage 当用户拒绝授权后弹出的Toast信息
* requestCode 请求码，当在多处申请权限时用来区分申请的权限
* showRationaleDialog 当用户拒绝授权，并且勾选不再提示后是否弹框跳转到设置页面，默认为true

##### 如果想接受权限拒绝的回调，请使用@APermissionDenied注解
```
 @APermissionDenied
    public void permissionDenied(Permission permission) {
    }
```
> Permission中封装了请求失败的权限，以及请求码

##### 如果你并不关心Permission的信息，你可以这样使用
```
@APermissionDenied
    public void permissionDenied() {
    }
```
##### 如果想接受拒绝权限后勾选不再提示的回调,请使用@APermissionRationale注解
```
 @APermissionRationale
    public void permissionRationale(Permission permission) {
    }
```
如果不关心Permission中的信息，也可以这样
```
 @APermissionRationale
    public void permissionRationale() {
    }
```

如果你根本就不关心回调的话，只想在拒绝后提示一下用户就完了，这两个注解都可以不使用，只需在@APermission指定deniedMessage就好了

### 三、一些使用例子
在activity中
 ```
@APermission(permissions = {Manifest.permission.BODY_SENSORS, Manifest.permission.READ_CALENDAR}, requestCode = 10)
    public void testRequestCode(View view) {
        Toast.makeText(this, "授权成功,执行接下来的代码", Toast.LENGTH_SHORT).show();
    }

    @APermissionDenied
    public void permissionDenied(Permission permission) {
        Log.e("399", "permissionDenied");
        Log.e("399", "code: " + permission.getRequestCode());
        Log.e("399", "list: " + permission.getRequestPermissions());
        Toast.makeText(this, "permissionDenied " + "code: " + permission.getRequestCode() + "list: " + permission.getRequestPermissions(), Toast.LENGTH_SHORT).show();
    }

    @APermissionRationale
    public void permissionRationale(Permission permission) {
        Log.e("399", "permissionRationale");
        Log.e("399", "code: " + permission.getRequestCode());
        Log.e("399", "list: " + permission.getRequestPermissions());
        Toast.makeText(this, "permissionRationale " + "code: " + permission.getRequestCode() + " list: " + permission.getRequestPermissions(), Toast.LENGTH_SHORT).show();

    }
```
在任意类中
```
 /**
     * 测试任意类申请权限
     *
     * @param view
     */
    public void testNormal(View view) {
        Test test = new Test();
        test.test();
    }

    public class Test {

        //测试授权失败显示的信息
        @APermission(permissions = {Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS},deniedMessage = "授权失败")
            public void test() {
              Log.e("399","有权限,执行某操作");
          }
            }
```

### 说明
* 在@APermission注解中指定了deniedMessage，同时使用了@APermissionDenied 或@APermissionRationale注解的方法，指定的deniedMessage就无效了，就直接回调注解的方法了

* 在@APermission注解的showRationaleDialog默认为true，所以当权限拒绝不在提示时后的弹框，并不影响后续回调

### 注：当在使用的类中有多个方法被@APermissionDenied 或者@APermissionRationale注解的话，并不会所有的方法都能接受到回调，所有建议一个类中只能有一个方法被@APermissionDenied 或@APermissionRationale注解，里面通过requestCode来判断
[博客地址](https://www.jianshu.com/p/4c00bddacf10)
如果使用发现问题，请及时反馈，谢谢！