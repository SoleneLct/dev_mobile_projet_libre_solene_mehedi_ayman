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

import org.o7planning.saeapplication.Activity.ViewFolderActivity;
import org.o7planning.saeapplication.Database.DataBaseImageManager;
import org.o7planning.saeapplication.Modele.Image;
import org.o7planning.saeapplication.R;

import java.util.List;
import java.util.Optional;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ViewFolderActivity viewFolderActivity;
    private List<Image> images;

    public ImageAdapter(List<Image> images, ViewFolderActivity viewFolderActivity) {
        this.images = images;
        this.viewFolderActivity = viewFolderActivity;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_template, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = images.get(position);
        Optional<Image> optionalImage = new DataBaseImageManager(this.viewFolderActivity)
                            .getAllImagesByUserIdAndFolder(image.getIdProfils(), image.getId()).stream().findFirst();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image.getImage(), 0, image.getImage().length);
        holder.folderName.setText(image.getNom());
        holder.folderImage.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView folderImage;
        public TextView folderName;

        public ImageViewHolder(View view) {
            super(view);
            folderImage = view.findViewById(R.id.folder_image);
            folderName = view.findViewById(R.id.folder_name);
        }

    }
}
