package com.mbientlab.metawear.metabase;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import org.apache.commons.lang3.ArrayUtils;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class baseline {

    //String modelFile="test_model_lstm7.tflite";
    String modelFile="model (1).tflite";
    Interpreter tflite;

    float[][] out = new float[][]{{0,0,0,0}};

    public void run_model(Context context, List<List<Float>> inp_t) {

        float[][] array = inp_t.stream().map(u -> ArrayUtils.toPrimitive(u.toArray(new Float[0]))).toArray(float[][]::new);
        float[][][] inp = new float[1][array.length][3];
        inp[0] = array;

        System.out.println("inp: "+ Arrays.deepToString(inp[0]));
        System.out.println("inp: "+ (inp[0].length));

        try {
            tflite = new Interpreter(loadModelFile(context, modelFile));
            System.out.println("model read");
        } catch (IOException e) {
            e.printStackTrace();
        }

        tflite.run(inp, out);

        float[] result = out[0];
        System.out.println("out: "+ Arrays.toString(result));
    }

    private MappedByteBuffer loadModelFile(Context myContext, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = myContext.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


}
