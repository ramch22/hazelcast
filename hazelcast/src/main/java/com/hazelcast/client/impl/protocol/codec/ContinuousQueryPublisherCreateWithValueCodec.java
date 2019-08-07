/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.client.impl.protocol.codec;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.codec.builtin.*;

import java.util.ListIterator;

import static com.hazelcast.client.impl.protocol.ClientMessage.*;
import static com.hazelcast.client.impl.protocol.codec.builtin.FixedSizeTypesCodec.*;

/**
 * TODO DOC
 */
public final class ContinuousQueryPublisherCreateWithValueCodec {
    //hex: 0x1801
    public static final int REQUEST_MESSAGE_TYPE = 6145;
    //hex: 0x0075
    public static final int RESPONSE_MESSAGE_TYPE = 117;
    private static final int REQUEST_BATCH_SIZE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_BUFFER_SIZE_FIELD_OFFSET = REQUEST_BATCH_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_DELAY_SECONDS_FIELD_OFFSET = REQUEST_BUFFER_SIZE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_POPULATE_FIELD_OFFSET = REQUEST_DELAY_SECONDS_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int REQUEST_COALESCE_FIELD_OFFSET = REQUEST_POPULATE_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_COALESCE_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = CORRELATION_ID_FIELD_OFFSET + LONG_SIZE_IN_BYTES;

    private ContinuousQueryPublisherCreateWithValueCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Name of the map.
         */
        public java.lang.String mapName;

        /**
         * Name of the cache for query cache.
         */
        public java.lang.String cacheName;

        /**
         * The predicate to filter events which will be applied to the QueryCache.
         */
        public com.hazelcast.nio.serialization.Data predicate;

        /**
         * The size of batch. After reaching this minimum size, node immediately sends buffered events to QueryCache.
         */
        public int batchSize;

        /**
         * Maximum number of events which can be stored in a buffer of partition.
         */
        public int bufferSize;

        /**
         * The minimum number of delay seconds which an event waits in the buffer of node.
         */
        public long delaySeconds;

        /**
         * Flag to enable/disable initial population of the QueryCache.
         */
        public boolean populate;

        /**
         * Flag to enable/disable coalescing. If true, then only the last updated value for a key is placed in the
         * batch, otherwise all changed values are included in the update.
         */
        public boolean coalesce;
    }

    public static ClientMessage encodeRequest(java.lang.String mapName, java.lang.String cacheName, com.hazelcast.nio.serialization.Data predicate, int batchSize, int bufferSize, long delaySeconds, boolean populate, boolean coalesce) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(true);
        clientMessage.setAcquiresResource(false);
        clientMessage.setOperationName("ContinuousQuery.PublisherCreateWithValue");
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeInt(initialFrame.content, REQUEST_BATCH_SIZE_FIELD_OFFSET, batchSize);
        encodeInt(initialFrame.content, REQUEST_BUFFER_SIZE_FIELD_OFFSET, bufferSize);
        encodeLong(initialFrame.content, REQUEST_DELAY_SECONDS_FIELD_OFFSET, delaySeconds);
        encodeBoolean(initialFrame.content, REQUEST_POPULATE_FIELD_OFFSET, populate);
        encodeBoolean(initialFrame.content, REQUEST_COALESCE_FIELD_OFFSET, coalesce);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, mapName);
        StringCodec.encode(clientMessage, cacheName);
        DataCodec.encode(clientMessage, predicate);
        return clientMessage;
    }

    public static ContinuousQueryPublisherCreateWithValueCodec.RequestParameters decodeRequest(ClientMessage clientMessage) {
        ListIterator<ClientMessage.Frame> iterator = clientMessage.listIterator();
        RequestParameters request = new RequestParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        request.batchSize = decodeInt(initialFrame.content, REQUEST_BATCH_SIZE_FIELD_OFFSET);
        request.bufferSize = decodeInt(initialFrame.content, REQUEST_BUFFER_SIZE_FIELD_OFFSET);
        request.delaySeconds = decodeLong(initialFrame.content, REQUEST_DELAY_SECONDS_FIELD_OFFSET);
        request.populate = decodeBoolean(initialFrame.content, REQUEST_POPULATE_FIELD_OFFSET);
        request.coalesce = decodeBoolean(initialFrame.content, REQUEST_COALESCE_FIELD_OFFSET);
        request.mapName = StringCodec.decode(iterator);
        request.cacheName = StringCodec.decode(iterator);
        request.predicate = DataCodec.decode(iterator);
        return request;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * TODO DOC
         */
        public java.util.List<java.util.Map.Entry<com.hazelcast.nio.serialization.Data, com.hazelcast.nio.serialization.Data>> response;
    }

    public static ClientMessage encodeResponse(java.util.Collection<java.util.Map.Entry<com.hazelcast.nio.serialization.Data, com.hazelcast.nio.serialization.Data>> response) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        clientMessage.add(initialFrame);

        MapCodec.encode(clientMessage, response, DataCodec::encode, DataCodec::encode);
        return clientMessage;
    }

    public static ContinuousQueryPublisherCreateWithValueCodec.ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ListIterator<ClientMessage.Frame> iterator = clientMessage.listIterator();
        ResponseParameters response = new ResponseParameters();
        //empty initial frame
        iterator.next();
        response.response = MapCodec.decode(iterator, DataCodec::decode, DataCodec::decode);
        return response;
    }

}
