
module CaseClassModule
	
	def case_class(sym, &block)
		sym.instance_eval &block
 	end

end

class Object

	include CaseClassModule

	def self.const_missing(nombre)
		Object.const_set(nombre.to_s, CaseClass.new)
	end

end