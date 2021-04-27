package com.zuci.zuciapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import se.simbio.encryption.Encryption;

public class Methods {
    private static Encryption encryptionChat;

    public static boolean isEmptyOrNull(String value) {
        return value == null || value.isEmpty() || value.matches("");
    }

    public static void initEncryptionChat(String secretKey, String secretSalt) {
        try {
            encryptionChat = Encryption.getDefault(secretKey, secretSalt, new byte[16]);
        } catch (Exception e) {
            Log.e("Method: initEncryption", e.getMessage());
        }
    }

    public static String encryptChatData(String encryptInput, Long chatRoomId, long senderId, long receiverId) {
        long hashCodeOne = String.valueOf(senderId).hashCode();
        long hashCodeTwo = String.valueOf(receiverId).hashCode();
        String saltKey = hashCodeOne < hashCodeTwo ? String.valueOf(senderId) : String.valueOf(receiverId);
        initEncryptionChat(String.valueOf(chatRoomId), saltKey);
        return encryptInput != null ? encryptionChat.encryptOrNull(encryptInput) : "";
    }

    public static String decryptChatData(String encryptInput, Long chatRoomId, long senderId, long receiverId) {
        long hashCodeOne = String.valueOf(senderId).hashCode();
        long hashCodeTwo = String.valueOf(receiverId).hashCode();
        String saltKey = hashCodeOne < hashCodeTwo ? String.valueOf(senderId) : String.valueOf(receiverId);
        initEncryptionChat(String.valueOf(chatRoomId), saltKey);
        String decryptedString = encryptionChat.decryptOrNull(encryptInput);
        return decryptedString != null ? decryptedString : "";
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String dateToFormatedString(Date date) {
        String pattern = "dd-MM-yyyy hh:mm aaa";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static Uri getImageUri(Activity activity, Bitmap selectedImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), selectedImage, "Title", null);
        return Uri.parse(path);
    }
}
