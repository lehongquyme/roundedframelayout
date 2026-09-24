# GradientStrokeFrameLayout

An Android `FrameLayout` with a rounded gradient border and a transparent center.

## Features

- Three-color gradient stroke
- Transparent center
- Configurable stroke width and corner radius
- Horizontal, vertical, or diagonal gradient
- Optional rounded clipping for child views
- Runtime configuration from Kotlin or Java
- No third-party runtime dependencies

## Install with JitPack

Add JitPack to `settings.gradle`:

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to the app module:

```groovy
dependencies {
    implementation 'com.github.lehongquyme:GradientStrokeFrameLayout:1.0.0'
}
```

For Kotlin DSL:

```kotlin
implementation("com.github.lehongquyme:GradientStrokeFrameLayout:1.0.0")
```

## XML usage

```xml
<com.quylh.gradientstrokelayout.GradientStrokeFrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="64dp"
    app:gslStrokeWidth="2dp"
    app:gslCornerRadius="14dp"
    app:gslStartColor="#FABB73"
    app:gslCenterColor="#FFA750"
    app:gslEndColor="#D27433"
    app:gslOrientation="topToBottom"
    app:gslClipContent="true">

    <!-- Your content. The layout itself does not fill the center. -->

</com.quylh.gradientstrokelayout.GradientStrokeFrameLayout>
```

## Kotlin usage

```kotlin
binding.gradientFrame.apply {
    setGradientStrokeWidth(2f * resources.displayMetrics.density)
    setGradientCornerRadius(14f * resources.displayMetrics.density)
    setGradientColors(
        Color.parseColor("#FABB73"),
        Color.parseColor("#FFA750"),
        Color.parseColor("#D27433")
    )
    setGradientOrientation(
        GradientStrokeFrameLayout.Orientation.TOP_TO_BOTTOM
    )
}
```

## Publish on JitPack

1. Create a GitHub repository named `GradientStrokeFrameLayout`.
2. Push this project to the repository's root directory.
3. Create and push a release tag:

```bash
git tag 1.0.0
git push origin 1.0.0
```

4. Open `https://jitpack.io/#lehongquyme/GradientStrokeFrameLayout/1.0.0` and click **Get it**.
5. Use the dependency shown above after the JitPack build succeeds.

If the GitHub repository name changes, replace `GradientStrokeFrameLayout` in the
dependency coordinate and in `artifactId` inside `library/build.gradle`.

## License

MIT License. See [LICENSE](LICENSE).
