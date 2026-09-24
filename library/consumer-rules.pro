# Keep the custom view constructor when an app enables code shrinking.
-keep public class com.quylh.gradientstrokelayout.GradientStrokeFrameLayout {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
