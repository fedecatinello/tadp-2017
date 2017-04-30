
module PatternModule

  class ::Object

    def _
      AnyPattern.new
    end

    def is_a(klass)
      belongPattern = BelongPattern.new
      belongPattern.clase_a_comparar = klass
      belongPattern
    end

    def has(*args)

      if args.length != 2
        throw 'ArgumentMismatchError: se esperaban 2 argumentos como máximo'
      end

      containsPattern = ContainsPattern.new
      containsPattern.argumento_a_comparar = {'nombre' => args[0], 'valor' => args[1]}
      containsPattern
    end

  end

  class BelongPattern
    attr_accessor :clase_a_comparar

    def ===(objeto_a_comparar)
      objeto_a_comparar.is_a? clase_a_comparar
    end
  end

  class AnyPattern
    def ===(*)
      true
    end
  end

  class ContainsPattern
    attr_accessor :argumento_a_comparar

    def initialize
      @argumento_a_comparar = {}
    end

    def ===(objeto_a_comparar)
      objeto_a_comparar.instance_variables.any? do |variable|

        variable.to_s[1..-1] == @argumento_a_comparar['nombre'].to_s &&
            objeto_a_comparar.instance_variable_get(variable) == @argumento_a_comparar['valor']

      end
    end
  end

end