package net.fenzz.dingplug;

import android.content.Context;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by mr.lee on 2017/3/24.
 */

public class Reflect {
    public interface ReflectResouce {
        public View findViewById(int viewId);
    }

    /**
     * 注意Java中只有值传递。
     * @param target
     * @param mconContext
     */
    public static final void reflectView(ReflectResouce target ,Context mContext){
        Field[] fields = target.getClass().getDeclaredFields();//得到所有属性，注意target.getClass() 得到是具体堆内存里边的对象的类型
        for (Field field : fields) {
            final String fieldName = field.getName();//得到属性名称
            View childView = target.findViewById(getResoucesId(mContext, fieldName));//得到view控件对象
            try {
                if(View.class.isAssignableFrom(childView.getClass())){//如果childView是View的子类
                    boolean accessible = field.isAccessible();//得到属性的访问权限
                    field.setAccessible(true);//打开修改权限
                    try {
                        field.set(target, childView);//这一步是关键，最后的结果是target.field=childView
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(accessible);
                }
            } catch (Exception e) {
                new IllegalArgumentException("the childView is not the child of View").printStackTrace();
            }
        }
    }
    /**
     * get the id value of view, According to view name
     * @param mContext
     * @param fName
     * @return 根据反射得到view的id值
     */
    public static final int getResoucesId(Context mContext , String fName){
        return mContext.getResources().getIdentifier(fName, "id", mContext.getPackageName());
    }
}

