package org.o7planning.saeapplication.Activity.Other;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.o7planning.saeapplication.Activity.ViewAllFolderActivity;
import org.o7planning.saeapplication.Activity.ViewFolderActivity;
import org.o7planning.saeapplication.Database.DataBaseImageManager;
import org.o7planning.saeapplication.Modele.Folder;
import org.o7planning.saeapplication.Modele.Image;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

import java.util.List;
import java.util.Optional;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private ViewAllFolderActivity viewAllFolderActivity;
    private List<Folder> folders;

    private Profil userObject;

    public FolderAdapter(List<Folder> folders, ViewAllFolderActivity viewAllFolderActivity, Profil userObject) {
        this.folders = folders;
        this.viewAllFolderActivity = viewAllFolderActivity;
        this.userObject = userObject;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder_template, parent, false);
        FolderViewHolder folderViewHolder = new FolderViewHolder(itemView);
        return folderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folderObject = folders.get(position);
        Optional<Image> optionalImage = new DataBaseImageManager(this.viewAllFolderActivity)
                            .getAllImagesByUserIdAndFolder(userObject.getId(), folderObject.getId()).stream().findFirst();
        Bitmap bitmap;
        if(optionalImage.isPresent()){
            bitmap = BitmapFactory.decodeByteArray(optionalImage.get().getImage(), 0, optionalImage.get().getImage().length);
        }
        else{
            Resources res = this.viewAllFolderActivity.getResources();
            bitmap = BitmapFactory.decodeResource(res, R.drawable.default_floder);
        }
        holder.folderName.setText(folderObject.getTitle());
        holder.folderImage.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageFolderActivity = new Intent(viewAllFolderActivity, ViewFolderActivity.class);
                imageFolderActivity.putExtra("userObject" , userObject);
                imageFolderActivity.putExtra("folderObject" ,folderObject);
                viewAllFolderActivity.startActivity(imageFolderActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        public ImageView folderImage;
        public TextView folderName;

        public FolderViewHolder(View view) {
            super(view);
            folderImage = view.findViewById(R.id.folder_image);
            folderName = view.findViewById(R.id.folder_name);
        }

    }
}
