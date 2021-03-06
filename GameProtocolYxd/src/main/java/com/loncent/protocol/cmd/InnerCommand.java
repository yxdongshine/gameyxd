// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: InnerCommand.proto

package com.loncent.protocol.cmd;

public final class InnerCommand {
  private InnerCommand() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum CmdInnerType
      implements com.google.protobuf.ProtocolMessageEnum {
    SC_SS_REGISTER_REQUEST(0, 1048576),
    S_L_CHECK_TOKEN_REQUEST(1, 1048577),
    S_L_CHECK_TOKEN_RESPONSE(2, 1048578),
    S_L_ONLIN_COUNT_REQUEST(3, 1048579),
    WORLD_GAME_ENTER_GAME_REQUEST(4, 1048580),
    GAME_GATE_ENTER_GAME_SUCCESS_RESPONSE(5, 1048581),
    GATE_GAME_PLAYER_EXIT_REQUEST(6, 1048582),
    GAME_WORLD_PLAYER_EXIT_REQUEST(7, 1048583),
    GAME_WORLD_UPDATE_PLAYER_REQUEST(8, 1048584),
    GATE_WORLD_PLAYER_EXIT_REQUEST(9, 1048585),
    INNER_SERVER_HEART_REQUEST(10, 1048586),
    WORLD_GAME_DEL_ROLE_REQUEST(11, 1048587),
    ;
    
    public static final int SC_SS_REGISTER_REQUEST_VALUE = 1048576;
    public static final int S_L_CHECK_TOKEN_REQUEST_VALUE = 1048577;
    public static final int S_L_CHECK_TOKEN_RESPONSE_VALUE = 1048578;
    public static final int S_L_ONLIN_COUNT_REQUEST_VALUE = 1048579;
    public static final int WORLD_GAME_ENTER_GAME_REQUEST_VALUE = 1048580;
    public static final int GAME_GATE_ENTER_GAME_SUCCESS_RESPONSE_VALUE = 1048581;
    public static final int GATE_GAME_PLAYER_EXIT_REQUEST_VALUE = 1048582;
    public static final int GAME_WORLD_PLAYER_EXIT_REQUEST_VALUE = 1048583;
    public static final int GAME_WORLD_UPDATE_PLAYER_REQUEST_VALUE = 1048584;
    public static final int GATE_WORLD_PLAYER_EXIT_REQUEST_VALUE = 1048585;
    public static final int INNER_SERVER_HEART_REQUEST_VALUE = 1048586;
    public static final int WORLD_GAME_DEL_ROLE_REQUEST_VALUE = 1048587;
    
    
    public final int getNumber() { return value; }
    
    public static CmdInnerType valueOf(int value) {
      switch (value) {
        case 1048576: return SC_SS_REGISTER_REQUEST;
        case 1048577: return S_L_CHECK_TOKEN_REQUEST;
        case 1048578: return S_L_CHECK_TOKEN_RESPONSE;
        case 1048579: return S_L_ONLIN_COUNT_REQUEST;
        case 1048580: return WORLD_GAME_ENTER_GAME_REQUEST;
        case 1048581: return GAME_GATE_ENTER_GAME_SUCCESS_RESPONSE;
        case 1048582: return GATE_GAME_PLAYER_EXIT_REQUEST;
        case 1048583: return GAME_WORLD_PLAYER_EXIT_REQUEST;
        case 1048584: return GAME_WORLD_UPDATE_PLAYER_REQUEST;
        case 1048585: return GATE_WORLD_PLAYER_EXIT_REQUEST;
        case 1048586: return INNER_SERVER_HEART_REQUEST;
        case 1048587: return WORLD_GAME_DEL_ROLE_REQUEST;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<CmdInnerType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<CmdInnerType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<CmdInnerType>() {
            public CmdInnerType findValueByNumber(int number) {
              return CmdInnerType.valueOf(number);
            }
          };
    
    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.loncent.protocol.cmd.InnerCommand.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final CmdInnerType[] VALUES = {
      SC_SS_REGISTER_REQUEST, S_L_CHECK_TOKEN_REQUEST, S_L_CHECK_TOKEN_RESPONSE, S_L_ONLIN_COUNT_REQUEST, WORLD_GAME_ENTER_GAME_REQUEST, GAME_GATE_ENTER_GAME_SUCCESS_RESPONSE, GATE_GAME_PLAYER_EXIT_REQUEST, GAME_WORLD_PLAYER_EXIT_REQUEST, GAME_WORLD_UPDATE_PLAYER_REQUEST, GATE_WORLD_PLAYER_EXIT_REQUEST, INNER_SERVER_HEART_REQUEST, WORLD_GAME_DEL_ROLE_REQUEST, 
    };
    
    public static CmdInnerType valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private CmdInnerType(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:com.loncent.protocol.cmd.CmdInnerType)
  }
  
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\022InnerCommand.proto\022\030com.loncent.protoc" +
      "ol.cmd*\272\003\n\014CmdInnerType\022\034\n\026SC_SS_REGISTE" +
      "R_REQUEST\020\200\200@\022\035\n\027S_L_CHECK_TOKEN_REQUEST" +
      "\020\201\200@\022\036\n\030S_L_CHECK_TOKEN_RESPONSE\020\202\200@\022\035\n\027" +
      "S_L_ONLIN_COUNT_REQUEST\020\203\200@\022#\n\035WORLD_GAM" +
      "E_ENTER_GAME_REQUEST\020\204\200@\022+\n%GAME_GATE_EN" +
      "TER_GAME_SUCCESS_RESPONSE\020\205\200@\022#\n\035GATE_GA" +
      "ME_PLAYER_EXIT_REQUEST\020\206\200@\022$\n\036GAME_WORLD" +
      "_PLAYER_EXIT_REQUEST\020\207\200@\022&\n GAME_WORLD_U" +
      "PDATE_PLAYER_REQUEST\020\210\200@\022$\n\036GATE_WORLD_P",
      "LAYER_EXIT_REQUEST\020\211\200@\022 \n\032INNER_SERVER_H" +
      "EART_REQUEST\020\212\200@\022!\n\033WORLD_GAME_DEL_ROLE_" +
      "REQUEST\020\213\200@B\032\n\030com.loncent.protocol.cmd"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
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
