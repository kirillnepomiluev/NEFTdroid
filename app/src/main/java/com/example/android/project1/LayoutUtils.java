package com.example.android.project1;

import android.content.res.Resources;

/**
 * @author John L. Jegutanis
 */
public class LayoutUtils {

    /**
     * Qr-code size calculation
     */
    static public int calculateMaxQrCodeSize(Resources resources) {
        int qrPadding = resources.getDimensionPixelSize(R.dimen.qr_code_padding);
        int qrCodeViewSize = resources.getDimensionPixelSize(R.dimen.qr_code_size);
        return qrCodeViewSize - 2 * qrPadding;
    }
}
