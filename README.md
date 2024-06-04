/**
 * 扩展ImageView
 * <p>
 * 扩展功能一：扩展ScaleType属性
 * ScaleType 增加 leftCrop 、 topCrop、rightCrop、bottomCrop
 * leftCrop ： 垂直方向充满，均匀缩放，显示在控件左侧，超出控件部分进行裁剪，优先显示图片左侧内容
 * topCrop ： 水平方向充满，均匀缩放，显示在控件顶部，超出控件部分进行裁剪，优先显示图片顶部内容
 * rightCrop ： 垂直方向充满，均匀缩放，显示在控件右侧，超出控件部分进行裁剪，优先显示图片右侧内容
 * bottomCrop ： 水平方向充满，均匀缩放，显示在控件底部，超出控件部分进行裁剪，优先显示图片底部内容
 *
 * 使用：xml app:scaleType="topCrop"  code imageView.setScaleType(ExtendScaleImageView.ExtendScalType.TOP_CROP);
 *
 *  扩展功能二：增加 setGravity()方法，设置src 的显示位置（可以设置上下左右、水平和垂直居中，前提是scaleType设置为 matrix）
 *  使用：xml app:srcGravity="center_horizontal|right"   code imageView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
 */
