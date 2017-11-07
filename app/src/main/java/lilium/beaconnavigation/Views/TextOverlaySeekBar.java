package lilium.beaconnavigation.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;

import lilium.beaconnavigation.Interfaces.Configurator;
import lilium.beaconnavigation.R;

public class TextOverlaySeekBar extends android.support.v7.widget.AppCompatSeekBar {
    private final String _template;
    private final String _progressWildCard;
    private Configurator _configurator = null;
    private ProgressTransformer _transformer = null;
    private String _overlayText;
    private Paint paint = new Paint();

    public TextOverlaySeekBar (Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(getResources().getDimension(R.dimen.text_size));
        paint.setTextAlign(Paint.Align.CENTER);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextOverlaySeekBar,
                0, 0);

        _progressWildCard = attributes.getString(R.styleable.TextOverlaySeekBar_progressWildCard);
        _template = attributes.getString(R.styleable.TextOverlaySeekBar_template);
        final int seekBarMin = attributes.getInt(R.styleable.TextOverlaySeekBar_seekbarMin,0);
        final int seekBarMax = attributes.getInt(R.styleable.TextOverlaySeekBar_seekbarMax,200);

        setMax(seekBarMax - seekBarMin);

        _transformer = new ProgressTransformer() {
            @Override
            public int GetProgress() {
                return getProgress() + seekBarMin;
            }

            @Override
            public void SetProgress(int value) {
                TextOverlaySeekBar.super.setProgress(value - seekBarMin);
            }
        };

        attributes.recycle();

        _overlayText = _template.replace(_progressWildCard,Integer.toString(_transformer.GetProgress()));

        setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = _transformer.GetProgress();
                _overlayText = _template.replace(_progressWildCard,Integer.toString(progress));

                if(_configurator != null)
                {
                    _configurator.SetValue(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setProgress(int value)
    {
        if(_transformer != null)
        {
            _transformer.SetProgress(value);
        }
        else{
            super.setProgress(value);
        }
    }

    @Override
    protected void onDraw(Canvas c){
        super.onDraw(c);

        Rect progressBounds = getProgressDrawable().getBounds();

        float _centerX = progressBounds.exactCenterX();
        float _baselineY = progressBounds.bottom;

        c.drawText(_overlayText, _centerX, _baselineY,paint);
    }

    public void HookToConfig(Configurator configurator)
    {
        _configurator = configurator;
    }

    private interface ProgressTransformer {
        int GetProgress();
        void SetProgress(int value);
    }
}
