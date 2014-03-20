package main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ClearCacheDialogue extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(
				"Are you sure you want to clear your cache? Any data not uploaded to servers will be lost forever.")
				.setPositiveButton("Yes, please clear my cache!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Data.clearLocalData(getActivity(), true);
							}
						})
				.setNegativeButton(
						"Wait! Its was a mitake do NOT delete cache.",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

}
