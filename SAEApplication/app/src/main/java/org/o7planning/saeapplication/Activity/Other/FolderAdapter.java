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

import org.o7planning.saeapplication.Activity.MainActivity;
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

    private Profil mUtilisateur;

    public FolderAdapter(List<Folder> folders, ViewAllFolderActivity viewAllFolderActivity, Profil mUtilisateur) {
        this.folders = folders;
        this.viewAllFolderActivity = viewAllFolderActivity;
        this.mUtilisateur = mUtilisateur;
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
        Folder folder = folders.get(position);
        Optional<Image> optionalImage = new DataBaseImageManager(this.viewAllFolderActivity)
                            .getAllImagesByUserIdAndFolder(folder.getUserId(), folder.getId()).stream().findFirst();
        Image img = null;
        Bitmap bitmap;
        if(optionalImage.isPresent()){
            bitmap = BitmapFactory.decodeByteArray(img.getImage(), 0, img.getImage().length);
        }
        else{
            Resources res = this.viewAllFolderActivity.getResources();
            bitmap = BitmapFactory.decodeResource(res, R.drawable.poussiere);
        }
        holder.folderName.setText(folder.getTitle());
        holder.folderImage.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageFolderActivity = new Intent(viewAllFolderActivity, ViewFolderActivity.class);
                imageFolderActivity.putExtra("userObject" ,mUtilisateur);
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
