package com.test;

import com.bbin.common.util.MD5Utils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by mrt on 2019/12/28 0028 14:43
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = DigestUtils.md5DigestAsHex("aaa001".getBytes());
        System.out.println(s);
    }
}
