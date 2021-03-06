// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: OnLineCount.proto

package com.loncent.protocol.inner;

public final class OnLineCount {
  private OnLineCount() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface OnLineCountRequestOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional int32 currentAppId = 1;
    boolean hasCurrentAppId();
    int getCurrentAppId();
    
    // optional string serverId = 2;
    boolean hasServerId();
    String getServerId();
    
    // optional int32 onlineCount = 3;
    boolean hasOnlineCount();
    int getOnlineCount();
  }
  public static final class OnLineCountRequest extends
      com.google.protobuf.GeneratedMessage
      implements OnLineCountRequestOrBuilder {
    // Use OnLineCountRequest.newBuilder() to construct.
    private OnLineCountRequest(Builder builder) {
      super(builder);
    }
    private OnLineCountRequest(boolean noInit) {}
    
    private static final OnLineCountRequest defaultInstance;
    public static OnLineCountRequest getDefaultInstance() {
      return defaultInstance;
    }
    
    public OnLineCountRequest getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.loncent.protocol.inner.OnLineCount.internal_static_com_loncent_protocol_OnLineCountRequest_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.loncent.protocol.inner.OnLineCount.internal_static_com_loncent_protocol_OnLineCountRequest_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional int32 currentAppId = 1;
    public static final int CURRENTAPPID_FIELD_NUMBER = 1;
    private int currentAppId_;
    public boolean hasCurrentAppId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getCurrentAppId() {
      return currentAppId_;
    }
    
    // optional string serverId = 2;
    public static final int SERVERID_FIELD_NUMBER = 2;
    private java.lang.Object serverId_;
    public boolean hasServerId() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getServerId() {
      java.lang.Object ref = serverId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          serverId_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getServerIdBytes() {
      java.lang.Object ref = serverId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        serverId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional int32 onlineCount = 3;
    public static final int ONLINECOUNT_FIELD_NUMBER = 3;
    private int onlineCount_;
    public boolean hasOnlineCount() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getOnlineCount() {
      return onlineCount_;
    }
    
    private void initFields() {
      currentAppId_ = 0;
      serverId_ = "";
      onlineCount_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, currentAppId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getServerIdBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, onlineCount_);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, currentAppId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getServerIdBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, onlineCount_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.loncent.protocol.inner.OnLineCount.OnLineCountRequest parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.loncent.protocol.inner.OnLineCount.OnLineCountRequest prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.loncent.protocol.inner.OnLineCount.OnLineCountRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.loncent.protocol.inner.OnLineCount.internal_static_com_loncent_protocol_OnLineCountRequest_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.loncent.protocol.inner.OnLineCount.internal_static_com_loncent_protocol_OnLineCountRequest_fieldAccessorTable;
      }
      
      // Construct using com.loncent.protocol.inner.OnLineCount.OnLineCountRequest.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        currentAppId_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        serverId_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        onlineCount_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.loncent.protocol.inner.OnLineCount.OnLineCountRequest.getDescriptor();
      }
      
      public com.loncent.protocol.inner.OnLineCount.OnLineCountRequest getDefaultInstanceForType() {
        return com.loncent.protocol.inner.OnLineCount.OnLineCountRequest.getDefaultInstance();
      }
      
      public com.loncent.protocol.inner.OnLineCount.OnLineCountRequest build() {
        com.loncent.protocol.inner.OnLineCount.OnLineCountRequest result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.loncent.protocol.inner.OnLineCount.OnLineCountRequest buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.loncent.protocol.inner.OnLineCount.OnLineCountRequest result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.loncent.protocol.inner.OnLineCount.OnLineCountRequest buildPartial() {
        com.loncent.protocol.inner.OnLineCount.OnLineCountRequest result = new com.loncent.protocol.inner.OnLineCount.OnLineCountRequest(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.currentAppId_ = currentAppId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.serverId_ = serverId_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.onlineCount_ = onlineCount_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.loncent.protocol.inner.OnLineCount.OnLineCountRequest) {
          return mergeFrom((com.loncent.protocol.inner.OnLineCount.OnLineCountRequest)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.loncent.protocol.inner.OnLineCount.OnLineCountRequest other) {
        if (other == com.loncent.protocol.inner.OnLineCount.OnLineCountRequest.getDefaultInstance()) return this;
        if (other.hasCurrentAppId()) {
          setCurrentAppId(other.getCurrentAppId());
        }
        if (other.hasServerId()) {
          setServerId(other.getServerId());
        }
        if (other.hasOnlineCount()) {
          setOnlineCount(other.getOnlineCount());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              currentAppId_ = input.readInt32();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              serverId_ = input.readBytes();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              onlineCount_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional int32 currentAppId = 1;
      private int currentAppId_ ;
      public boolean hasCurrentAppId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getCurrentAppId() {
        return currentAppId_;
      }
      public Builder setCurrentAppId(int value) {
        bitField0_ |= 0x00000001;
        currentAppId_ = value;
        onChanged();
        return this;
      }
      public Builder clearCurrentAppId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        currentAppId_ = 0;
        onChanged();
        return this;
      }
      
      // optional string serverId = 2;
      private java.lang.Object serverId_ = "";
      public boolean hasServerId() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getServerId() {
        java.lang.Object ref = serverId_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          serverId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setServerId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        serverId_ = value;
        onChanged();
        return this;
      }
      public Builder clearServerId() {
        bitField0_ = (bitField0_ & ~0x00000002);
        serverId_ = getDefaultInstance().getServerId();
        onChanged();
        return this;
      }
      void setServerId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        serverId_ = value;
        onChanged();
      }
      
      // optional int32 onlineCount = 3;
      private int onlineCount_ ;
      public boolean hasOnlineCount() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getOnlineCount() {
        return onlineCount_;
      }
      public Builder setOnlineCount(int value) {
        bitField0_ |= 0x00000004;
        onlineCount_ = value;
        onChanged();
        return this;
      }
      public Builder clearOnlineCount() {
        bitField0_ = (bitField0_ & ~0x00000004);
        onlineCount_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:com.loncent.protocol.OnLineCountRequest)
    }
    
    static {
      defaultInstance = new OnLineCountRequest(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:com.loncent.protocol.OnLineCountRequest)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_com_loncent_protocol_OnLineCountRequest_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_loncent_protocol_OnLineCountRequest_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021OnLineCount.proto\022\024com.loncent.protoco" +
      "l\"Q\n\022OnLineCountRequest\022\024\n\014currentAppId\030" +
      "\001 \001(\005\022\020\n\010serverId\030\002 \001(\t\022\023\n\013onlineCount\030\003" +
      " \001(\005B\034\n\032com.loncent.protocol.inner"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_com_loncent_protocol_OnLineCountRequest_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_com_loncent_protocol_OnLineCountRequest_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_com_loncent_protocol_OnLineCountRequest_descriptor,
              new java.lang.String[] { "CurrentAppId", "ServerId", "OnlineCount", },
              com.loncent.protocol.inner.OnLineCount.OnLineCountRequest.class,
              com.loncent.protocol.inner.OnLineCount.OnLineCountRequest.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
