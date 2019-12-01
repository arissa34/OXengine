package rma.ox.data.network.http.example.request.post;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.ArrayMap;
import rma.ox.data.network.http.AbsRequest;
import rma.ox.data.network.http.ResponseError;
import rma.ox.data.network.http.example.data.PostData;
import rma.ox.engine.utils.Logx;

import java.util.HashMap;
import java.util.Map;

public class PostRequestTest extends AbsRequest<PostData> {

    private static final String TAG = PostRequestTest.class.getSimpleName();
    @Override
    protected String getUrl() {
        return "http://ptsv2.com/t/m7hi0-1552231279/post";
    }

    @Override
    protected String getMethod() {
        return Net.HttpMethods.POST;
    }

    @Override
    protected ArrayMap<String, String> getHeaders() {
        ArrayMap<String, String> headers = new ArrayMap<String, String>();
        headers.put("accept-encoding", "gzip, deflate");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    @Override
    protected Map<String, String> getPostParams() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("name", "morpheus");
        param.put("job", "leader");
        return param;
    }

    @Override
    protected Class<PostData> getTypeToken() {
        return PostData.class;
    }

    @Override
    protected boolean mustSave() {
        return true;
    }

    @Override
    protected boolean isFileToDownload() {
        return false;
    }

    @Override
    protected void saveResult(PostData result) throws Exception {
        Logx.d(this.getClass(),"==> saveResult !!");
    }

    @Override
    protected void postSuccesEvent(PostData result) {
        Logx.d(this.getClass(),"postSuccesEvent "+result.createdAt);
        Logx.d(this.getClass(),"postSuccesEvent "+result.id);
    }

    @Override
    protected void postFailEvent(ResponseError error) {

        Gdx.app.log(TAG, "postFailEvent "+error.networkResponse.getStatus().getStatusCode());
    }
}
