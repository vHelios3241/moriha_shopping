package com.moriha.shopping_webhooks.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlarmMessageDto {
  private int scopeId;
  private String scope;
  private String name;
  private String id0;
  private String id1;
  private String ruleName;
  private String alarmMessage;
  private List<Tag> tags;
  private long startTime;
  private transient int period;
  private transient boolean onlyAsCondition;
  @Data
  public static class Tag{
    private String key;
    private String value;
   }
}
