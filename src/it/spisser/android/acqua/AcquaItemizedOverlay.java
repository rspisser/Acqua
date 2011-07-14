package it.spisser.android.acqua;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;



public class AcquaItemizedOverlay extends ItemizedOverlay {

	Context mContext;

	public AcquaItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		// get the appropriate item

		Spanned t;
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		t=Html.fromHtml(item.getSnippet());
		dialog.setMessage(t);
		
		dialog.show();
		return true;
	}
}
