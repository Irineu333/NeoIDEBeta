package neo.ide.ui;

//extends Activity
import androidx.appcompat.app.AppCompatActivity;

import android.util.TypedValue;
import android.content.Context;
import android.os.PersistableBundle;
import android.os.Bundle;
import android.view.ViewGroup;
import android.util.DisplayMetrics;

public class BaseActivity extends AppCompatActivity
{
    protected static Context contextoAtual;
    
    @Deprecated
    protected float getDip(float _input){
        /* Copiado do Skethware */
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(contextoAtual != this)
            contextoAtual = this;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        contextoAtual = this;
    }
    protected DisplayMetrics getDisplayMetrics()
    {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics;
    }
    
    protected int getDisplayHeight()
    {
        return getDisplayMetrics().heightPixels;
    }
    
    protected int getDisplayWidth()
    {
        return getDisplayMetrics().widthPixels;
    }
}
