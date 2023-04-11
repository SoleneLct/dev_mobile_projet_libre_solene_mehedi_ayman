package org.o7planning.saeapplication.Activity.Other;

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
import org.o7planning.saeapplication.R;

import java.util.List;
import java.util.Optional;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.FolderViewHolder> {

    private ViewAllFolderActivity viewAllFolderActivity;
    private List<Image> images;

    public ImageAdapter(List<Image> images, ViewFolderActivity viewFolderActivity) {
        this.images = images;
        this.viewAllFolderActivity = viewAllFolderActivity;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder_template, parent, false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Image image = images.get(position);
        Optional<Image> optionalImage = new DataBaseImageManager(this.viewAllFolderActivity)
                            .getAllImagesByUserIdAndFolder(image.getIdProfils(), image.getId()).stream().findFirst();
        Image img = null;
        Bitmap bitmap;
        if(optionalImage.isPresent()){
            bitmap = BitmapFactory.decodeByteArray(img.getImage(), 0, img.getImage().length);
        }
        else{
            Resources res = this.viewAllFolderActivity.getResources();
            bitmap = BitmapFactory.decodeResource(res, R.drawable.poussiere);
        }
        holder.folderName.setText(image.getNom());
        holder.folderImage.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return images.size();
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
