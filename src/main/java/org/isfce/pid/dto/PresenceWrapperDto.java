package org.isfce.pid.dto;

import org.isfce.pid.model.Presence;
import org.isfce.pid.model.PresenceWrapper;

import java.util.List;

public class PresenceWrapperDto {
    private List<Presence> presences;

    public List<Presence> getPresences() {
        return presences;
    }

    public void setPresences(List<Presence> presences) {
        this.presences = presences;
    }
}
