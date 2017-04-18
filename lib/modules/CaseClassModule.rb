
module CaseClassModule

	def case_class(str, &block)

		# str es una concatenacion entre el simbolo inicial y del que hereda con un ~ en el medio
		# si no hereda str split va a tener length de 1

		arrSym = str.to_s.split('~')

		if arrSym.length == 1
			Object.const_get(arrSym[0].to_s).class_eval &block
			define_singleton_method(arrSym[0].to_s, lambda { | *args |

				klass = Object.const_get(arrSym[0].to_s)
				misVariables = klass.get_variables

				auxInstance = klass.new

				cont = 0
				args.map { |arg|
					auxInstance.instance_variable_set('@' + misVariables[cont].to_s, arg )
					cont += 1
				}

				auxInstance.freeze
				return auxInstance
			})
		elsif arrSym.length == 2
			puts 'Aca va la logica de heredar'
		else
			puts 'Aca hay que tirar un error extraño'
		end
	end

end

class Object

	include CaseClassModule

	def self.const_missing(name)
		Object.const_set(name.to_s, CaseClass.dup)
	end

=begin
	def self.method_missing(symbol, *args, &block)
		if symbol.is_case_class?
			puts symbol.to_s + 'is case class'
			#Object.const_get(symbol.to_s).class_eval &block
		else
			puts 'super'
		end
	end

	def self.respond_to_missing?(symbol, include_all=false)
		true
	end
=end

	def is_case_class?
		false
	end

end
