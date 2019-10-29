package com.wusy.wusylibrary.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import com.wusy.wusylibrary.util.permissions.PermissionsManager
import com.wusy.wusylibrary.util.permissions.PermissionsResultAction
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

/**
 * 这是一个快速实现相机拍照、照片裁剪的工具类
 */
class CameraUtil {

    val TAG: String = "CameraUtil"

    //拍照(返回原始图)
    companion object {
        var imageFile: File? = null     //拍照后保存的照片
        var imgUri: Uri? = null         //拍照后保存的照片的uri
        val AUTHORITY = "com.wusy.fileprovider" //记得配置7.0 的provider uri
        val REQUEST_CODE_CAPTURE_RAW = 6 //startActivityForResult时的请求码
    }

    /**
     * 打开系统相机
     */
    fun gotoCaptureRaw(activity: Activity) {
        imageFile = createImageFile()
        imageFile?.let {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //如果是7.0以上，使用FileProvider，否则会报错
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                imgUri = FileProvider.getUriForFile(activity, AUTHORITY, it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri) //设置拍照后图片保存的位置
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it)) //设置拍照后图片保存的位置
            }
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()) //设置图片保存的格式
            intent.resolveActivity(activity.packageManager)?.let {
                if (PermissionsManager.getInstance().hasPermission(activity, Manifest.permission.CAMERA)) {
                    activity.startActivityForResult(intent, REQUEST_CODE_CAPTURE_RAW) //调起系统相机
                } else {
                    Toast.makeText(activity, "打开相机失败，需要获取相机权限", Toast.LENGTH_SHORT).show()
                    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(activity,
                        arrayOf(Manifest.permission.CAMERA), object : PermissionsResultAction() {
                            override fun onGranted() {
                                activity.startActivityForResult(intent, REQUEST_CODE_CAPTURE_RAW) //调起系统相机
                            }

                            override fun onDenied(permission: String) {

                            }
                        })
                }
            }
        }
    }

    //生成一个文件
    private fun createImageFile(isCrop: Boolean = false): File? {
        return try {
            var rootFile = File(Environment.getExternalStorageDirectory().toString() + File.separator + "CameraPath")
            if (!rootFile.exists())
                rootFile.mkdirs()
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = if (isCrop) "IMG_${timeStamp}_CROP.jpg" else "IMG_$timeStamp.jpg"
            File(rootFile.absolutePath + File.separator + fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    //压缩图片并显示
    fun compressImage(file: File, listener: CallBack) {
        thread {
            var temp = System.currentTimeMillis()
            var bitmap = BitmapFactory.decodeFile(file.absolutePath)
            var compressBitmap = BitmapUtils.decodeBitmap(bitmap, 1080, 1080)
            Log.d("TAG", "原始图片大小  ${bitmap.width} * ${bitmap.height}")
            Log.d("TAG", "压缩后图片大小  ${compressBitmap.width} * ${compressBitmap.height}")
            Log.d("TAG", "加载图片耗时 ${System.currentTimeMillis() - temp}")
            listener.compressImageComplete(compressBitmap, file)
        }
    }

    open interface CallBack {
        fun compressImageComplete(imgBitmap: Bitmap, file: File)
    }

    /**
     * 压缩图片（质量压缩）
     * 压缩BitMap至指定大小以内，并且返回File
     * @param bitmap
     */
    fun compressImage(bitmap: Bitmap): File {
        var baos: ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            options -= 10//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            var length = baos.toByteArray().size
        }
        var format: SimpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        var date: Date = Date(System.currentTimeMillis())
        var filename = format.format(date)
        var file: File = File(Environment.getExternalStorageDirectory(), "$filename.png")
        try {
            var fos: FileOutputStream = FileOutputStream(file)
            try {
                fos.write(baos.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
//        recycleBitmap(bitmap)
        return file;
    }

    fun recycleBitmap(bm: Bitmap) {
        if (null != bm && !bm.isRecycled) {
            bm.recycle()
        }
    }

}
