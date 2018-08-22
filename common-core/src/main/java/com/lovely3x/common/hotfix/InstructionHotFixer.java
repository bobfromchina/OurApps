package com.lovely3x.common.hotfix;

import android.content.Context;
import android.util.Log;

import com.lovely3x.jsonparser.model.JSONObject;

import java.lang.ref.WeakReference;

/**
 * 热修护器
 * Created by lovely3x on 17/3/3.
 */
public abstract class InstructionHotFixer {

    /**
     * 清除补丁文件
     */
    public static final int CLEANUP_PATCH = 0x1;

    /**
     * 应用补丁
     */
    public static final int APPLY_PATCH = 0x2;


    /**
     * 通过自杀的方式来应用补丁，使其生效
     */
    public static final int APPLY_PATCH_STRATEGY_KILL = 1;

    /**
     * 当用户切换至后台后自杀来程序来使补丁生效
     */
    public static final int APPLY_PATCH_STRATEGY_BACKGROUND = 2;

    /**
     * 当用户启动app时引用补丁生效
     */
    public static final int APPLY_PATCH_STRATEGY_BOOTSTRAP = 3;

    private static final String TAG = "InstructionHotFixer";


    private static WeakReference<Context> mWeakContext;

    public static void init(Context context, InstructionHotFixer fixer) {
        if (context == null || fixer == null)
            throw new IllegalArgumentException("Context and fixer can't be null.");

        context = context.getApplicationContext() == null ? context : context.getApplicationContext();
        mWeakContext = new WeakReference<>(context);

        fixer.initialize();
    }


    protected boolean onReceivedInstructionCode(JSONObject instructionJson) {
        Instruction instruction = instructionJson.createObject(Instruction.class);
        Log.d(TAG, "Received instruction code " + instruction);
        
        switch (instruction.getOpCode()) {
            case APPLY_PATCH: {
                applyPatch(instruction);
                return true;
            }
            case CLEANUP_PATCH: {
                cleanupPatch(instruction);
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化修护器
     */
    protected abstract void initialize();

    /**
     * 子类实现加载补丁的过程
     *
     * @param instruction 指定对象
     */
    protected abstract void applyPatch(Instruction instruction);

    /**
     * 子类实现清除补丁的过程
     *
     * @param instruction 指定对象
     */
    protected abstract void cleanupPatch(Instruction instruction);

    protected Context getContext() {
        return mWeakContext == null ? null : mWeakContext.get();
    }

}
