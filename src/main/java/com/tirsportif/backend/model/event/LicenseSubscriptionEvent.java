package com.tirsportif.backend.model.event;

import com.tirsportif.backend.model.Licensee;
import lombok.Value;

@Value
public class LicenseSubscriptionEvent {

    Licensee licensee;

}
