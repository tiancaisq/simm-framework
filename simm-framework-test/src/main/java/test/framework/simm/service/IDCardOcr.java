package test.framework.simm.service;

import test.framework.simm.model.IDCardInfo;

import java.io.InputStream;

/**
 * 身份证识别
 */
public interface IDCardOcr {
    IDCardInfo getInfo(InputStream inputStream) throws Exception;
}
