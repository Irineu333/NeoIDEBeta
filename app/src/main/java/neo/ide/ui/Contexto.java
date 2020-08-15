package neo.ide.ui;
import androidx.annotation.NonNull;
import android.content.Context;

public class Contexto extends BaseActivity
{
    @Deprecated
    @NonNull
    public static Context getContext()
    {
        return contextoAtual;
    }
}
