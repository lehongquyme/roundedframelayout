# RoundedFrameLayout

Thư viện Android nhỏ gọn để tạo `FrameLayout` bo góc, có màu nền và viền, đồng thời cắt nội dung theo đúng kích thước thật của view. Thư viện không thêm compatibility padding như `CardView`.

## Import trực tiếp bằng AAR

Sau khi chạy `./gradlew assembleRelease`, lấy file:

```text
build/outputs/aar/rounded-frame-layout-release.aar
```

Chép file vào thư mục `app/libs` của project cần sử dụng, rồi thêm dependency:

```groovy
dependencies {
    implementation files('libs/rounded-frame-layout-release.aar')
}
```

## Cài bằng JitPack

Đẩy thư mục này thành một repository GitHub có tên `rounded-frame-layout`, sau đó tạo tag, ví dụ `1.0.0`.

Thêm JitPack vào `settings.gradle` của ứng dụng:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Thêm dependency, thay `YOUR_GITHUB_USERNAME` bằng tên tài khoản GitHub:

```groovy
dependencies {
    implementation 'com.github.YOUR_GITHUB_USERNAME:rounded-frame-layout:1.0.0'
}
```

## Cài bằng Maven Local

Trong project thư viện, chạy:

```bash
./gradlew publishToMavenLocal
```

Trong project ứng dụng, thêm `mavenLocal()` vào repositories và dependency:

```groovy
implementation 'com.quylh.ui:rounded-frame-layout:1.0.0'
```

## Sử dụng trong XML

```xml
<com.quylh.roundedframelayout.RoundedFrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/roundedContainer"
    android:layout_width="120dp"
    android:layout_height="120dp"
    app:roundedBackgroundColor="@android:color/white"
    app:roundedCornerRadius="16dp"
    app:roundedStrokeColor="@android:color/black"
    app:roundedStrokeWidth="2dp">

    <!-- Nội dung của bạn -->

</com.quylh.roundedframelayout.RoundedFrameLayout>
```

## Sử dụng bằng Kotlin

```kotlin
import com.quylh.roundedframelayout.RoundedFrameLayout

roundedContainer.setStrokeColor(Color.RED)
roundedContainer.setStroke(resources.getDimensionPixelSize(R.dimen.stroke_width), Color.BLUE)
roundedContainer.setCornerRadius(resources.getDimension(R.dimen.corner_radius))
roundedContainer.setFillColor(Color.WHITE)
```

`setCornerRadius()` và `setStrokeWidth()` nhận giá trị pixel. Khi lấy từ resource, hãy dùng `getDimension()` hoặc `getDimensionPixelSize()` như ví dụ trên.
