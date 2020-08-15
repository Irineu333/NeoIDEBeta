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

public class MainActivity extends BaseActivity
{
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomSheetLayout;
    private ViewGroup.LayoutParams layoutParams;
    
    private int oldState;

    //teste
    float pos;
    String state;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomSheetLayout = findViewById(R.id.bottomSheet);
        
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
                    updateBottomSheetHeight(0);
                }
            });
        
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){

                @Override
                public void onStateChanged(View p1, int state)
                {
                    String[] estados = new String[6];
                    
                    estados[BottomSheetBehavior.STATE_DRAGGING-1] = "dragging";
                    estados[BottomSheetBehavior.STATE_SETTLING-1] = "settling";
                    estados[BottomSheetBehavior.STATE_EXPANDED-1] = "expanded";
                    estados[BottomSheetBehavior.STATE_COLLAPSED-1] = "collapsed";
                    estados[BottomSheetBehavior.STATE_HIDDEN-1] = "hidden";
                    estados[BottomSheetBehavior.STATE_HALF_EXPANDED-1] = "half_expanded";
                    
                    MainActivity.this.state = estados[state-1];
                    
                    text.setText(MainActivity.this.pos + " | " + MainActivity.this.state);
                    
                    switch(state)
                    {
                        case BottomSheetBehavior.STATE_HALF_EXPANDED:
                            bottomSheetFull(false);
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            bottomSheetFull(true);
                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:
                            //abrir novamente
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;
                        default:
                        break;
                    }
                    //estado anterior que importa
                    if(state!=BottomSheetBehavior.STATE_DRAGGING&&state!=BottomSheetBehavior.STATE_SETTLING)
                    oldState = state;
                }

                @Override
                public void onSlide(View p1, float pos)
                {
                    MainActivity.this.pos = pos;
                    text.setText(MainActivity.this.pos + " | " + state);
                    
                    //parar no half_expanded na descida
                    if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_SETTLING)
                        if(pos<0.6&&pos>0.4&&oldState==BottomSheetBehavior.STATE_EXPANDED)
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                   
                    //redimensionar
                    updateBottomSheetHeight(pos);
                }
                
                
            });
        
        ImageButton expandir = findViewById(R.id.expandir);

        expandir.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view)
                {
                    //implementar
                }
            });
 
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    
    private void updateBottomSheetHeight(float pos)
    {
        int toHeight;
        float toPos;
        
        if(layoutParams==null)
        layoutParams = bottomSheetLayout.getLayoutParams();
        
        if(pos<=0.5)
            toPos = 0.5f;
           else
             toPos = pos;
             
        toHeight = (int) (getDisplayHeight()*toPos);
        
        layoutParams.height = toHeight;
        bottomSheetLayout.setLayoutParams(layoutParams);
    }
}
