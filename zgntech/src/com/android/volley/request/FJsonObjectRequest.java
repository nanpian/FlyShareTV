/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Listener;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;

import java.io.UnsupportedEncodingException;

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL,
 * allowing for an optional {@link JSONObject} to be passed in as part of the
 * request body.
 */
public class FJsonObjectRequest extends JsonRequest<JSONObject> {
	/**
	 * Creates a new request.
	 * 
	 * @param method
	 *            the HTTP method to use
	 * @param url
	 *            URL to fetch the JSON from
	 * @param jsonRequest
	 *            A {@link JSONObject} to post with the request. Null is allowed
	 *            and indicates no parameters will be posted along with request.
	 * @param listener
	 *            Listener to receive the JSON response or error message
	 */
	public FJsonObjectRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener) {
		super(method, url, (jsonRequest == null) ? null : jsonRequest
				.toString(), listener);
	}

	/**
	 * Constructor which defaults to <code>GET</code> if
	 * <code>jsonRequest</code> is <code>null</code>, <code>POST</code>
	 * otherwise.
	 *
	 * @see #JsonObjectRequest(int, String, JSONObject, Listener)
	 */
	public FJsonObjectRequest(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener) {
		this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
				listener);
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, response.charset);
			return Response.success(JSON.parseObject(jsonString), response);
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}
}
