package com.alexstyl.specialdates.images;

import com.nostra13.universalimageloader.BuildConfig;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;

import java.io.IOException;
import java.io.InputStream;

class NutraBaseImageDecoder extends BaseImageDecoder {

    NutraBaseImageDecoder() {
        super(BuildConfig.DEBUG);
    }

    @Override
    protected InputStream getImageStream(ImageDecodingInfo decodingInfo) throws IOException {
        InputStream stream = decodingInfo.getDownloader()
                .getStream(decodingInfo.getImageUri(), decodingInfo.getExtraForDownloader());
        return stream == null ? null : new JpegClosedInputStream(stream);
    }

    private class JpegClosedInputStream extends InputStream {

        private static final int JPEG_EOI_1 = 0xFF;
        private static final int JPEG_EOI_2 = 0xD9;
        private final InputStream inputStream;
        private int bytesPastEnd;

        private JpegClosedInputStream(final InputStream iInputStream) {
            inputStream = iInputStream;
            bytesPastEnd = 0;
        }

        @Override
        public int read() throws IOException {
            int buffer = inputStream.read();
            if (buffer == -1) {
                if (bytesPastEnd > 0) {
                    buffer = JPEG_EOI_2;
                } else {
                    ++bytesPastEnd;
                    buffer = JPEG_EOI_1;
                }
            }

            return buffer;
        }
    }
}
