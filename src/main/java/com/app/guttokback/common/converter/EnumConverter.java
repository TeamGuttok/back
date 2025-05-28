package com.app.guttokback.common.converter;

import com.app.guttokback.common.security.Roles;
import com.app.guttokback.email.domain.enums.EmailType;
import com.app.guttokback.notification.domain.enums.Category;
import com.app.guttokback.notification.domain.enums.Status;
import com.app.guttokback.userSubscription.domain.enums.PaymentCycle;
import com.app.guttokback.userSubscription.domain.enums.PaymentMethod;
import com.app.guttokback.userSubscription.domain.enums.PaymentStatus;
import com.app.guttokback.userSubscription.domain.enums.Subscription;
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

    @Converter(autoApply = true)
    public static class CategoryConverter extends EnumConverter<Category> {
        public CategoryConverter() {
            super(Category.class);
        }
    }

    @Converter(autoApply = true)
    public static class StatusConverter extends EnumConverter<Status> {
        public StatusConverter() {
            super(Status.class);
        }
    }

    @Converter(autoApply = true)
    public static class EmailTypeConverter extends EnumConverter<EmailType> {
        public EmailTypeConverter() {
            super(EmailType.class);
        }
    }

    @Converter(autoApply = true)
    public static class RoleTypeConverter extends EnumConverter<Roles> {
        public RoleTypeConverter() {
            super(Roles.class);
        }
    }
}
