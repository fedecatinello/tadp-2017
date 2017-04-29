module CaseClassMixin

  def lalala
    ancestors = self.class.ancestors

    flag = false
    result = ancestors.select do |elem|
      if (elem == CaseClassMixin)
        flag = true
        next
      end
      if (elem == Object)
        flag = false
        next
      end
      flag
    end
    result
  end

  def is_method_in_ancestors?(method)

    ancestors = self.class.ancestors

    flag = false
    result = ancestors.select do |elem|
      if (elem == CaseClassMixin)
        flag = true
        next
      end
      if (elem == Object)
        flag = false
        next
      end
      flag
    end

    result.any? { |ancestor|
      ancestor.instance_methods(false).include? method
    }

  end

  # Metodos utilizados para los buenos defaults

  def to_s

    # if self.is_method_in_ancestors? :to_s
    #   super
    # else
      variables = self.instance_variables
      if variables.length
        # implementación vieja: BUG con los array
        # "#{self.class}(#{self.instance_variables.map { |var| self.instance_variable_get(var) }
        #                    .join(', ')})"
        result = self.class.to_s + '('
        self.instance_variables.each_with_index { |var, i|
          value = self.instance_variable_get(var)
          result += value == nil ? 'nil' : value.to_s
          if i + 1 < variables.length
            result += ', '
          end
        }
        result += ')'
        result
      else
        self.class.to_s + '()'
      end
    end
  # end

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