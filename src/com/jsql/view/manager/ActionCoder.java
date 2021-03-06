/*******************************************************************************
 * Copyhacked (H) 2012-2014.
 * This program and the accompanying materials
 * are made available under no term at all, use it like
 * you want, but share and discuss about it
 * every time possible with every body.
 *
 * Contributors:
 *      ron190 at ymail dot com - initial implementation
 *******************************************************************************/
package com.jsql.view.manager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

import com.jsql.tool.ToolsString;

/**
 * Action runned when this.coderManager.encoding.
 */
public class ActionCoder implements ActionListener {
    private ManagerCoder coderManager;
    
    public ActionCoder(ManagerCoder coderManager) {
        super();
        this.coderManager = coderManager;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (Arrays.asList(new String[]{ "Md2", "Md5", "Sha-1", "Sha-256", "Sha-384", "Sha-512" } ).contains(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue().replace(" hash", ""))) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue().replace(" hash", ""));
            } catch (NoSuchAlgorithmException e1) {
                this.coderManager.result.setText("No such algorithm for hashes exists");
            }

            String passwordString = new String(this.coderManager.entry.getText().toCharArray());
            byte[] passwordByte = passwordString.getBytes();
            md.update(passwordByte, 0, passwordByte.length);
            byte[] encodedPassword = md.digest();
            String encodedPasswordInString = this.coderManager.digestToHexString(encodedPassword);

            this.coderManager.result.setText(encodedPasswordInString);
        } else if ("Mysql".equals(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue().replace(" hash", ""))) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("sha-1");
            } catch (NoSuchAlgorithmException e1) {
                this.coderManager.result.setText("No such algorithm for hashes exists");
            }

            String password = new String(this.coderManager.entry.getText().toCharArray());
            byte[] passwordBytes = password.getBytes();
            md.update(passwordBytes, 0, passwordBytes.length);
            byte[] hashSHA1 = md.digest();
            String stringSHA1 = this.coderManager.digestToHexString(hashSHA1);

            String passwordSHA1 = new String(ToolsString.hexstr(stringSHA1).toCharArray());
            byte[] passwordSHA1Bytes = passwordSHA1.getBytes();
            md.update(passwordSHA1Bytes, 0, passwordSHA1Bytes.length);
            byte[] hashSHA1SH1 = md.digest();
            String mysqlHash = this.coderManager.digestToHexString(hashSHA1SH1);

            this.coderManager.result.setText(mysqlHash);
        } else if ("hex encode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            try {
                this.coderManager.result.setText(Hex.encodeHexString(this.coderManager.entry.getText().getBytes("UTF-8")).trim());
            } catch (UnsupportedEncodingException e) {
                this.coderManager.result.setText("this.coderManager.encoding error: " + e.getMessage());
            }
        } else if ("hex decode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            try {
                this.coderManager.result.setText(new String(Hex.decodeHex(this.coderManager.entry.getText().toCharArray()), "UTF-8"));
            } catch (Exception e) {
                this.coderManager.result.setText("Decoding error: " + e.getMessage());
            }
        } else if ("hex(zipped) encode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            try {
                this.coderManager.result.setText(Hex.encodeHexString(this.coderManager.compress(this.coderManager.entry.getText()).getBytes("UTF-8")).trim());
            } catch (Exception e) {
                this.coderManager.result.setText("this.coderManager.encoding error: " + e.getMessage());
            }
        } else if ("hex(zipped) decode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            try {
                this.coderManager.result.setText(this.coderManager.decompress(new String(Hex.decodeHex(this.coderManager.entry.getText().toCharArray()), "UTF-8")));
            } catch (Exception e) {
                this.coderManager.result.setText("Decoding error: " + e.getMessage());
            }
        } else if ("base64(zipped) encode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            try {
                this.coderManager.result.setText(this.coderManager.base64Encode(this.coderManager.compress(this.coderManager.entry.getText())));
            } catch (IOException e) {
                this.coderManager.result.setText("this.coderManager.encoding error: " + e.getMessage());
            }
        } else if ("base64(zipped) decode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            try {
                this.coderManager.result.setText(this.coderManager.decompress(this.coderManager.base64Decode(this.coderManager.entry.getText())));
            } catch (IOException e) {
                this.coderManager.result.setText("Decoding error: " + e.getMessage());
            }
        } else if ("base64 encode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            this.coderManager.result.setText(this.coderManager.base64Encode(this.coderManager.entry.getText()));
        } else if ("base64 decode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            this.coderManager.result.setText(this.coderManager.base64Decode(this.coderManager.entry.getText()));
        } else if ("html encode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            this.coderManager.result.setText(StringEscapeUtils.escapeHtml3(this.coderManager.entry.getText()));
        } else if ("html decode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            this.coderManager.result.setText(StringEscapeUtils.unescapeHtml3(this.coderManager.entry.getText()));
        } else if ("url encode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            try {
                this.coderManager.result.setText(URLEncoder.encode(this.coderManager.entry.getText(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                this.coderManager.result.setText("this.coderManager.encoding error: " + e.getMessage());
            }
        } else if ("url decode".equalsIgnoreCase(((ComboItem) this.coderManager.encoding.getSelectedItem()).getValue())) {
            try {
                this.coderManager.result.setText(URLDecoder.decode(this.coderManager.entry.getText().replace("%", "%25"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                this.coderManager.result.setText("Decoding error: " + e.getMessage());
            }
        }
    }
}