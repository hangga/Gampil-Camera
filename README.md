# Gampil-Camera
Embed camera preview with API Camera2 and surfaceview, just paste in layout. It's very easy. 

<img width="300" src="https://github.com/hangga/Gampil-Camera/blob/main/skrinsut.png?raw=true"/>

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
gampilPreview.setCameraFacing(GampilPreview.CAMERA_FRONT); // Default
```

```java
gampilPreview.setCameraFacing(GampilPreview.CAMERA_BACK);
```


- **Take Picture**
```java
gampilPreview.takePicture(new GampilPreview.OnTakePicture() {
    @Override
    public void onPictureTaken(File file, Bitmap bitmap) {
        // Your code
    }
});
```
## Sample Project
- <a href="https://github.com/hangga/Gampil-Sample" target="_blank">checkout</a>
