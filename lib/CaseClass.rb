class CaseClass

  @variables = nil

	def metodo_de_la_caseclass
		'hola'
	end

  def self.< (sym)
		return (self.ancestors.first.to_s + '~' + sym.to_s).to_s
	end

  def self.is_case_class?
		true
	end

  def self.attr_accessor (*attrs)
		@variables = attrs # Guardo los atributos en una variable de clase para poder pedirlas desde afuera
		attrs.map { |attr|
			self.send('attr_reader', attr)
			self.instance_variable_set('@' + attr.to_s, ':D')
		}
	end

	def self.get_variables
		return @variables
	end

  def to_s
		aux_string = ''
		aux_string += self.class.to_s
		aux_string += '('
		self.class.get_variables.each{ |var|
			aux_string += self.instance_variable_get('@' + var.to_s) + ', '
		}
		aux_string = aux_string.chomp(', ')
		aux_string += ')'
		return aux_string
	end

	def hash
		hash_aux = 7
		self.class.get_variables.each{ |var|
			hash_aux += self.instance_variable_get('@' + var.to_s).hash
		}
		return hash_aux
	end

	def ==(other_instance)
		instance = self

		if (instance.class != other_instance.class)
			return false
		elsif (instance.instance_variables != other_instance.instance_variables)
			return false
		else
			instance.instance_variables.each { |var|
				if(instance.send(var.to_s[1..-1]) != other_instance.send(var.to_s[1..-1])) # string[1..-1] remueve el primer caracter, en este caso un @
					return false
				end
			}
			return true
		end
	end

	def initialize(*args)

		instance_variables = self.class.get_variables

		cont = 0

		args.map { |arg| # Por cada argumento que se le pasa a la funcion (valor a setear)
			# Se setea cada variable del array instance_variables con cada argumento del array args en orden
			self.instance_variable_set('@' + instance_variables[cont].to_s, arg)
			cont += 1
		}

		self.freeze

	end

	private

	def create_inmutable_instance(case_class, args)

		aux_instance = case_class.new # Instancia que se va a devolver luego de prepararla

		instance_variables = case_class.get_variables # Un array con el nombre de todas las variables que tiene la clase

		cont = 0
		args.map { |arg| # Por cada argumento que se le pasa a la funcion (valor a setear)
			# Se setea cada variable del array instance_variables con cada argumento del array args en orden
			aux_instance.instance_variable_set('@' + instance_variables[cont].to_s, arg)
			cont += 1
		}

		# Finalmente se freeza la instancia y se devuelve
		aux_instance.freeze
	end

end

