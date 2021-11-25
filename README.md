# Gampil-Camera
Embed camera preview with API Camera and surfaceview, just paste in layout. It's very easy. 

<img width="300" src="https://github.com/hangga/Gampil-Camera/blob/main/capture-2021-11-25-200734.png?raw=true"/>

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
- **Add GampilPreview to your activity layout**
```xml
<id.hangga.gampil.GampilPreview
    android:id="@+id/gampilPreview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
- **Initialization**
```java
GampilPreview gampilPreview = findViewById(R.id.gampilPreview);
```

- **Set Camera Facing**  

```java
gampilPreview.setFacing(Facing.FRONT_CAMERA); // Default
```

```java
gampilPreview.setFacing(Facing.BACK_CAMERA);
```


- **Take Picture**  
Params:
quality – Compress bitmap quality  
takePhotoListener – CallBack after takePicture
```java
public void takePhoto(int quality, TakePhotoListener takePhotoListener)
```
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
- <a href="https://github.com/hangga/Gampil-Sample" target="_blank">checkout</a>
