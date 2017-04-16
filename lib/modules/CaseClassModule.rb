
module CaseClassModule
	
	def case_class(sym, &block)
		sym.class_eval &block
 	end

end

class Object

	include CaseClassModule

	def self.const_missing(name) # TODO: Evaluar nombre comience con CC.
		if ( name.to_s.split('_').first == 'CC' )
			Object.const_set(name.to_s, CaseClass.dup)
		else
			super
		end
	end

end