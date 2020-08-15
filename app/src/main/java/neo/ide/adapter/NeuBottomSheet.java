package neo.ide.adapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.annotation.Nullable;
import android.view.View;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.annotation.IdRes;
import androidx.annotation.Dimension;
import androidx.annotation.LayoutRes;

/*
 * Created by Irineu A. Silva (Neu)
 * mmmirineusilva@gmail.com
 * 61 9 8156-8035
 */

public class NeuBottomSheet extends BottomSheetDialogFragment
{

	private int bottomSheetPeekHeight; //altura minima do bottomsheet
	private View dialogContainer; //referencia do container do dialog
	private int content;

	private NeuBottomSheet.Action action;
	private NeuBottomSheet.StateChanged stateChanged;

	private boolean isShow;

	public NeuBottomSheet(@Dimension int peekHeight, @LayoutRes int resource)
	{
		content = resource;
		bottomSheetPeekHeight = peekHeight;
	}

	@Nullable @Override public View onCreateView(
		@NonNull LayoutInflater inflater,
		@Nullable ViewGroup container,
		@Nullable Bundle savedInstanceState)
	{

		isShow = true;

		View view = inflater.inflate(content, container, false);

		if (action != null)
			action.onCreate(view);

		return view;
	}

	private void initChanged()
	{
		BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(dialogContainer);
		bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){

				@Override
				public void onStateChanged(View view, int state)
				{
					switch (state)
					{
						case BottomSheetBehavior.STATE_COLLAPSED:
							NeuBottomSheet.this.stateChanged.onCollapsed(NeuBottomSheet.this.getView(), dialogContainer);
							break;
						case BottomSheetBehavior.STATE_EXPANDED:
							NeuBottomSheet.this.stateChanged.onExpanded(NeuBottomSheet.this.getView(), dialogContainer);
							break;
						case BottomSheetBehavior.STATE_HIDDEN:
							NeuBottomSheet.this.dismiss();
							break;
						default:
							break;
					}
				}

				@Override
				public void onSlide(View p1, float p2)
				{}
			});
	}

	@Override public void onResume()
	{
		super.onResume();
		dialogContainer = (View) getView().getParent(); // no onResume pra evitar getParent() return null

		if (action != null)
			action.onResume(getView(), dialogContainer);

		if (stateChanged != null)
			initChanged();

		peekUpBottomSheet(); //
	}

	public void setAction(Action action)
	{
		this.action = action;
	}

	public boolean isShow()
	{
		return isShow;
	}

	public void turnState()
	{
		BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(dialogContainer);
		if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
			collapseBottomSheet();
		else
			expandBottomSheet();

	}

	public void turnState(View indicador)
	{
		BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(dialogContainer);
		if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
		{
			collapseBottomSheet();
			indicador.setRotation((float)0);
		}
		else
		{
			indicador.setRotation((float)180);
			expandBottomSheet();
		}
	}

	public void peekUpBottomSheet()
	{
		BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(dialogContainer);
		bottomSheetBehavior.setPeekHeight(bottomSheetPeekHeight);
	}

	public void expandBottomSheet()
	{
		BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(dialogContainer);
		bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
	}

	public void collapseBottomSheet()
	{
		BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(dialogContainer);
		bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
	}

	public void setOnStateChanged(final StateChanged stateChanged)
	{
		this.stateChanged = stateChanged;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		isShow = false;
	}

	public static interface Action
	{
		public void onCreate(View content);
		public void onResume(View content, View container);
	}

	public static interface StateChanged
	{
		public void onCollapsed(View content, View container);
		public void onExpanded(View content, View container);
	}
}
