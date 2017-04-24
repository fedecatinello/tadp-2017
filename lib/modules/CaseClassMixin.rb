module CaseClassMixin

  # Metodos utilizados para los buenos defaults

  def to_s
    "#{self.class}(#{self.instance_variables.map { |var| self.instance_variable_get(var) }
                         .join(', ')})"
  end

  def hash
    self.instance_variables.inject(7) { |result, var| result + self.instance_variable_get(var).hash }
  end

  def ==(other_instance) # TODO: Refactorizar
    instance = self

    if (instance.class != other_instance.class)
      return false
    elsif (instance.instance_variables != other_instance.instance_variables)
      return false
    else
      instance.instance_variables.each do |var|
        if(instance.send(var.to_s[1..-1]) != other_instance.send(var.to_s[1..-1])) # string[1..-1] remueve el primer caracter, en este caso un @
          return false
        end
      end
      return true
    end
  end

  # Metodo utilizado para el copiado inteligente de instancias

  def copy(*args) # args pueden ser una o mas funciones lambda

    new_instance = self.dup

    args.each do |lam| # Checkeo que todas las lambda sean de aridad 1
      if lam.arity != 1
        raise 'Las funciones lambda deben ser de aridad 1.'
      end
    end

    args.each do |lam|
      # El unico argumento de cada lambda es el nombre del atributo que quiere modificar

      attr_name = lam.parameters.first.last.to_s # parameters es un array asi: [[:req, :nombre_de_variable]] por eso .first.last

      # La lambda solo se aplica si el parametro (nombre de variable a modificar) esta definida en la instancia,
      # si no se podrian crear nuevas variables a una instancia pasando en la lambda un nombre que no existe como
      # variable (porque se hace un instance_variable_set).
      if self.instance_variable_defined? ('@'+attr_name)
        new_instance.instance_variable_set('@'+attr_name, lam[self.instance_variable_get('@'+attr_name)]) # lam[] aplica la funcion a un valor
      end

    end

    new_instance.freeze
  end

end