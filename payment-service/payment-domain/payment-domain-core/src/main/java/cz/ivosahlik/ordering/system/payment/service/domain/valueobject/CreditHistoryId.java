package cz.ivosahlik.ordering.system.payment.service.domain.valueobject;

import cz.ivosahlik.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {
    public CreditHistoryId(UUID value) {
        super(value);
    }
}
