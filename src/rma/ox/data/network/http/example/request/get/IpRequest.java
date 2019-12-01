package rma.ox.data.network.http.example.request.get;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.ArrayMap;
import rma.ox.data.network.http.AbsRequest;
import rma.ox.data.network.http.ResponseError;
import rma.ox.data.network.http.example.data.IpData;
import rma.ox.engine.utils.Logx;

import java.util.Map;

public class IpRequest extends AbsRequest<IpData> {

    @Override
    protected String getUrl() {
        return "http://ip.jsontest.com";
    }

    @Override
    protected String getMethod() {
        return Net.HttpMethods.GET;
    }

    @Override
    protected ArrayMap<String, String> getHeaders() {
        return null;
    }

    @Override
    protected Map<String, String> getPostParams() {
        return null;
    }

    @Override
    protected Class<IpData> getTypeToken() {
        return IpData.class;
    }

    @Override
    protected boolean mustSave() {
        return false;
    }

    @Override
    protected boolean isFileToDownload() {
        return false;
    }

    @Override
    protected void saveResult(IpData result) throws Exception {

    }

    @Override
    protected void postSuccesEvent(IpData result) {
        Logx.d(this.getClass(),"postSuccesEvent "+result.ip);
    }

    @Override
    protected void postFailEvent(ResponseError error) {
        Logx.d(this.getClass(),"postFailEvent");
    }
}
