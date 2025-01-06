package com.app.guttokback.global.converter;

import com.app.guttokback.subscription.domain.PaymentCycle;
import com.app.guttokback.subscription.domain.PaymentMethod;
import com.app.guttokback.subscription.domain.PaymentStatus;
import com.app.guttokback.subscription.domain.Subscription;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EnumConverter<T extends Enum<T>> implements AttributeConverter<T, String> {

    private final Class<T> enumType;

    public EnumConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.name() : null; // Enum -> String
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Enum.valueOf(enumType, dbData); // String -> Enum
    }

    @Converter(autoApply = true)
    public static class PaymentCycleConverter extends EnumConverter<PaymentCycle> {
        public PaymentCycleConverter() {
            super(PaymentCycle.class);
        }
    }

    @Converter(autoApply = true)
    public static class PaymentMethodConverter extends EnumConverter<PaymentMethod> {
        public PaymentMethodConverter() {
            super(PaymentMethod.class);
        }
    }

    @Converter(autoApply = true)
    public static class PaymentStatusConverter extends EnumConverter<PaymentStatus> {
        public PaymentStatusConverter() {
            super(PaymentStatus.class);
        }
    }

    @Converter(autoApply = true)
    public static class SubscriptionConverter extends EnumConverter<Subscription> {
        public SubscriptionConverter() {
            super(Subscription.class);
        }
    }
}