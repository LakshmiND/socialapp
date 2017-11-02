package com.example.ldevarak.socialapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldevarak on 10/25/2017.
 */


public class EditOrDeleteLinkListner implements View.OnLongClickListener {
    Context context;
    String id;

    @Override
    public boolean onLongClick(View view) {


        context = view.getContext();
        id = view.getTag().toString();

        final CharSequence[] items = {"Edit", "Delete"};

        new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Dialog))
                .setTitle("Edit/Delete Link")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            editRecord(Integer.parseInt(id));
                            // ((MainActivity) context).displayLinksView(true, Integer.parseInt(id));
                        } else if (item == 1) {
                            boolean deleteSuccessful = new TableControllerLinks(context).delete(Integer.parseInt(id));
                            if (deleteSuccessful) {
                                Toast.makeText(context, "Link deleted successfully.", Toast.LENGTH_SHORT).show();
                                ((MainActivity) context).displayLinksView(true, Integer.parseInt(id),null);
                            } else {
                                Toast.makeText(context, "Unable to delete the Link.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        dialog.dismiss();

                    }
                }).show();
        return false;
    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void editRecord(final int linkId) {
        final TableControllerLinks tableControllerLinks = new TableControllerLinks(context);
        LinksObject linksObject = tableControllerLinks.readSingleRecord(linkId);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.links_input_form, null, false);
        final EditText eLinknameTxt = (EditText) formElementsView.findViewById(R.id.e_linkname_txt);
        final EditText eUrlTxt = (EditText) formElementsView.findViewById(R.id.e_url_txt);
        eLinknameTxt.setText(linksObject.linkname);
        eUrlTxt.setText(linksObject.url);
        String category=linksObject.category;
        final String favourite=linksObject.favourite;

        final Spinner spinner = (Spinner) formElementsView.findViewById(R.id.e_spinner_txt);
        List<String> categories = new ArrayList<String>();
        categories.add("Media");
        categories.add("Education");
        categories.add("Ecomerce");
        categories.add("Helth care");
        categories.add("Personal");
        categories.add("Travel");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setSelection(getIndex(spinner, "Education"));
        spinner.setAdapter(dataAdapter);
        String myString =category;
        for (int i = 0; i < dataAdapter.getCount(); i++) {
            if (myString.trim().equals(dataAdapter.getItem(i).toString())) {
                spinner.setSelection(i);
                break;
            }
        }


        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Edit Record")
                .setPositiveButton("Save Changes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                LinksObject linksObject1 = new LinksObject();
                                linksObject1.id = linkId;
                                linksObject1.linkname = eLinknameTxt.getText().toString();
                                linksObject1.url = eUrlTxt.getText().toString();
                                linksObject1.category = spinner.getSelectedItem().toString();
                                linksObject1.favourite =favourite;
                                boolean updateSuccessful = tableControllerLinks.update(linksObject1);
                                ((MainActivity) context).displayLinksView(false, 0,null);
                                if (updateSuccessful) {
                                    Toast.makeText(context, "Link updated successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Unable to update the Link .", Toast.LENGTH_SHORT).show();
                                }

                                dialog.cancel();
                            }

                        }).show();
    }

}
