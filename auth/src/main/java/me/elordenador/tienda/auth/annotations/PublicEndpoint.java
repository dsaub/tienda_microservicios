package me.elordenador.tienda.auth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
public @interface PublicEndpoint {
}
