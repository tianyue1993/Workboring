package com.etcomm.dcare.netresponse;

import com.etcomm.dcare.app.model.Topic;

import java.util.List;

/**
 * topic 话题--- discuss 讨论 --- comment 评论
 *
 * @author iexpressbox
 */
public class DisscussContent extends Content {
    private List<DisscussItems> items;

    private int page_count;

    private Topic topic;


    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setItems(List<DisscussItems> items) {
        this.items = items;
    }

    public List<DisscussItems> getItems() {
        return this.items;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getPage_count() {
        return this.page_count;
    }


}
