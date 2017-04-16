
module CaseClassModule
	
	def case_class(str, &block)

		# str es una concatenacion entre el simbolo inicial y del que hereda con un ~ en el medio
		# si no hereda str split va a tener length de 1

		arrSym = str.to_s.split('~')

		if (arrSym.length == 1)
			Object.const_get(arrSym[0].to_s).class_eval &block
		elsif (arrSym.length == 2)
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

end