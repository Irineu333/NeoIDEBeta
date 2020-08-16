package neo.ide.ui;

import android.os.Bundle;

//classe R
import neo.ide.beta.R;

//minhas classes

//imports de teste
import android.widget.TextView;
import android.view.View.*;
import android.widget.Toast;
import neo.ide.adapter.NeuBottomSheet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.ViewGroup.LayoutParams;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class MainActivity extends BaseActivity
{
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheetLayout;
    private ViewGroup.LayoutParams bottomSheetLayoutParams;
    private CoordinatorLayout viewRoot;

    private int oldState;

    //teste
    float pos;
    String state;

    private ViewGroup.LayoutParams codeEditorLayoutParams;
    private View codeEditorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomSheetLayout = findViewById(R.id.bottomSheet);
        codeEditorLayout = findViewById(R.id.codeEditor);
        //viewRoot = findViewById(R.id.viewRoot);

        //configurar
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setFitToContents(false);

        //teste
        final TextView text = findViewById(R.id.teste);

        //incializar apos renderizar o layout
        runOnUiThread(new Runnable(){

                @Override
                public void run()
                {
                    bottomSheetHeightUpdate(0);
                    layoutUpdate(0);
                }
            });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){

                @Override
                public void onStateChanged(View p1, int state)
                {
                    String[] estados = new String[6];

                    estados[BottomSheetBehavior.STATE_DRAGGING - 1] = "dragging";
                    estados[BottomSheetBehavior.STATE_SETTLING - 1] = "settling";
                    estados[BottomSheetBehavior.STATE_EXPANDED - 1] = "expanded";
                    estados[BottomSheetBehavior.STATE_COLLAPSED - 1] = "collapsed";
                    estados[BottomSheetBehavior.STATE_HIDDEN - 1] = "hidden";
                    estados[BottomSheetBehavior.STATE_HALF_EXPANDED - 1] = "half_expanded";

                    MainActivity.this.state = estados[state - 1];

                    text.setText(MainActivity.this.pos + " | " + MainActivity.this.state);

                    switch (state)
                    {
                        case BottomSheetBehavior.STATE_HALF_EXPANDED:
                            bottomSheetHeightUpdate(0.5f);
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            bottomSheetHeightUpdate(1);
                            break;
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            bottomSheetHeightUpdate(0);
                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:
                            //abrir novamente
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;
                        default:
                            break;
                    }
                    //estado anterior que importa
                    if (state != BottomSheetBehavior.STATE_DRAGGING && state != BottomSheetBehavior.STATE_SETTLING)
                        oldState = state;
                }

                @Override
                public void onSlide(View p1, float pos)
                {
                    MainActivity.this.pos = pos;
                    text.setText(MainActivity.this.pos + " | " + state);

                    //parar no half_expanded na descida
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING)
                        if (pos < 0.6 && pos > 0.4 && oldState == BottomSheetBehavior.STATE_EXPANDED)
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

                    //redimensionar
                    bottomSheetHeightUpdate(pos);
                    layoutUpdate(pos);
                }


            });

        //ImageButton expandir = findViewById(R.id.expandir);
        /*
        expandir.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view)
                {
                    //implementar
                }
            });
            */
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    private void bottomSheetHeightUpdate(float pos)
    {
        int toHeight;
        float toPos;

        if (bottomSheetLayoutParams == null)
            bottomSheetLayoutParams = bottomSheetLayout.getLayoutParams();

        if (pos <= 0.5f)
            toPos = 0.5f;
        else
            toPos = pos;

        toHeight = (int) (getDisplayHeightWithoutStatusBar() * toPos);

        int peek = (int) (bottomSheetBehavior.getPeekHeight()*toPos);
        
        bottomSheetLayoutParams.height = toHeight + peek;
        bottomSheetLayout.setLayoutParams(bottomSheetLayoutParams);
    }

    private void layoutUpdate(float pos)
    {
        float toPos;
        int toHeight;
        if (codeEditorLayoutParams == null)
            codeEditorLayoutParams = codeEditorLayout.getLayoutParams();

        if (pos >= 0.5f)
            toPos = 0.5f;
        else
            toPos = 1 - pos;
            
        toHeight = (int) (getDisplayHeightWithoutStatusBar() * toPos);
        
        int peek = (int) (bottomSheetBehavior.getPeekHeight()*toPos);
        
        codeEditorLayoutParams.height = toHeight - peek;
        codeEditorLayout.setLayoutParams(codeEditorLayoutParams);
    }

    private int getDisplayHeightWithoutStatusBar()
    {
        return getDisplayHeight() - (int)getDip(24);
    }
}
