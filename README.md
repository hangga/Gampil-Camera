# Gampil Camera
A simple library to make it easier for you to embed a camera and surfaceview in an activity in your application. 

## Setup

**Gradle**
```gradle
allprojects {
    repositories {
    	...
	maven { url 'https://jitpack.io' }
    }
}
```

```gradle
dependencies {
    implementation 'com.github.hangga:Gampil-Camera:v1.1.2'
}


```
## How to use
### 1. Add GampilPreview to your activity layout
```xml
<id.hangga.gampil.GampilPreview
    android:id="@+id/gampilPreview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
### 2. Initialization
Initiate object in your java code. 
```java
GampilPreview gampilPreview = findViewById(R.id.gampilPreview);
```
### 3. Method
Methods you can use. 
#### 3.1. Set Camera Facing 
  You can choose front or back camera easily.   

```java
gampilPreview.setFacing(Facing.FRONT_CAMERA); // Default
```

```java
gampilPreview.setFacing(Facing.BACK_CAMERA);
```


#### 3.2. Take Picture
**takePhoto(int quality, TakePhotoListener takePhotoListener)**   
Params:
quality – Compress bitmap quality  
takePhotoListener – CallBack after takePicture   
- An example of how to capture a photo is like the code below.   
   
```java
gampilPreview.takePhoto(80, new TakePhotoListener() {
    @Override
    public void onPhotoTaken(Bitmap bitmap, File file) {
        // your code
    }

    @Override
    public void onPhotoError(String message) {
        // your code
    }
});
```

## Sample Project
<img width="300" src="https://github.com/hangga/Gampil-Camera/blob/main/capture-2021-11-25-200734.png?raw=true"/>  
- <a href="https://github.com/hangga/Gampil-Sample" target="_blank">checkout</a>
