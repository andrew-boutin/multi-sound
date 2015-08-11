package com.andrewboutin.multisound;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Andrew on 8/9/2015.
 */
public class SoundAdapter extends ArrayAdapter<Sound>{
    private DBHandler dbHandler;

    public SoundAdapter(Context context, ArrayList<Sound> sounds) {
        super(context, 0, sounds);
        dbHandler = DBHandler.getDbHandler(context);
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent){
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sound, parent, false);

        Sound sound = (Sound)getItem(index);

        TextView name = (TextView) convertView.findViewById(R.id.tvName);
        name.setText(sound.getName());

        setUpEditButton(convertView, sound);

        setUpDeleteButton(convertView, sound);

        convertView.setClickable(true);
        convertView.setFocusable(true);
        convertView.setBackgroundResource(android.R.drawable.menuitem_background);

        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // TODO: Play sound file
                Toast.makeText(v.getContext(), "This will play the sound file!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    // TODO: Make alert edit file name as well
    private void setUpEditButton(View convertView, final Sound sound){
        Button editButton = (Button)convertView.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Edit Sound");

                final EditText nameEdit = new EditText(v.getContext());
                nameEdit.setText(sound.getName());

                final EditText fileEdit = new EditText(v.getContext());
                fileEdit.setText(sound.getFileName());

                LinearLayout lila1 = new LinearLayout(v.getContext());
                lila1.setOrientation(LinearLayout.VERTICAL);
                lila1.addView(nameEdit);
                lila1.addView(fileEdit);

                builder.setView(lila1);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEdit.getText().toString();
                        String fileName = fileEdit.getText().toString();

                        if (name.equals("") || fileName.equals("")) {
                            Toast.makeText(v.getContext(), "Blank input not allowed!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        sound.setName(name);
                        sound.setFileName(fileName);

                        dbHandler.updateSound(sound);

                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.create().show();
            }
        });
    }

    private void setUpDeleteButton(View convertView, final Sound sound){
        Button deleteButton = (Button)convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirm deletion of '" + sound.getName() +"'");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(sound);
                        dbHandler.deleteSound(sound);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
}
