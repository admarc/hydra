package com.github.admarc.validator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

public class UniquePropertyValidator implements ConstraintValidator<UniqueProperty, Object> {

    private UniqueProperty constraintAnnotation;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(UniqueProperty uniqueEntity) {
        this.constraintAnnotation = uniqueEntity;
    }

    @Override
    public boolean isValid(final Object entity, ConstraintValidatorContext context) {

        if (entityManager == null) {
            return true;
        }

        Class<?> entityClass = entity.getClass();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
        Root<?> root = criteriaQuery.from(entityClass);

        try {
            Object propertyValue = getPropertyValue(entityClass, entity, constraintAnnotation.name());

            Predicate uniquePropertyPredicate = criteriaBuilder.equal(
                    root.get(constraintAnnotation.name()),
                    propertyValue
            );
            Field idField = entityClass.getDeclaredField("id");
            String idProperty = idField.getName();
            Object idValue = getPropertyValue(entityClass, entity, idProperty);
            if (idValue != null) {
            Predicate idNotEqualsPredicate = criteriaBuilder.notEqual(root.get(idProperty), idValue);
            criteriaQuery.select(root).where(uniquePropertyPredicate, idNotEqualsPredicate);
            } else {
                criteriaQuery.select(root).where(uniquePropertyPredicate);
            }

        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format(
                            "Unable to fetch the data for property '%s' in entity '%s' for @UniqueProperty",
                            constraintAnnotation.name(),
                            entityClass
                    ),
                    exception
            );
        }

        List<Object> resultSet = entityManager.createQuery(criteriaQuery).getResultList();

        if (!resultSet.isEmpty()) {
            ConstraintViolationBuilder constraintViolationBuilder =
                    context.buildConstraintViolationWithTemplate(constraintAnnotation.message());
            ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderContext =
                    constraintViolationBuilder.addPropertyNode(constraintAnnotation.name());
            ConstraintValidatorContext constraintValidatorContext = nodeBuilderContext.addConstraintViolation();
            constraintValidatorContext.disableDefaultConstraintViolation();

            return false;
        }

        return true;
    }

    private Object getPropertyValue(Class<?> entityClass, Object entity, String propertyName)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String propertyGetter = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

        Method getterMethod = entityClass.getMethod(propertyGetter);

        return getterMethod.invoke(entity, (Object[])null);
    }

}
