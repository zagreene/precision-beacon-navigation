package lilium.beaconnavigation.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class kludgeListView extends ListView {
    public kludgeListView(Context context) {
        super(context);
    }

    public kludgeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public kludgeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        try{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        catch(IndexOutOfBoundsException ex)
        {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }

    }

    @Override
    protected void layoutChildren() {
        try {
            super.layoutChildren();
        } catch (Exception IllegalStateException){
            //This is thrown when the ListView dataset is not the same size as the adapter array
            //The proper way to avoid this is to adapter.notifyDataSetChanged();
            //but that will cause the ListView to dynamically change, making it hard for the user to select a beacon
            //Just ignore the exception to avoid a crash! (hopefully nothing else goes wrong)
        }
    }
}
